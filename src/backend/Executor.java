package backend;

import javafx.scene.control.Alert;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.*;

public class Executor {
    int times = 0;
    int side = 0;//电脑方向0白1黑
    int maxDepth = 4;
    ChessBoard chessBoard;
    MoveGenerator generator;
    Move temp;
    Logger logger;

    HashMap<Integer, String> s = new HashMap<>();
    EvaluateEngine eva = new EvaluateEngine();
    StringBuilder builder = new StringBuilder();
    public Executor() {
        Zobrist.initItalian();
        chessBoard = new ChessBoard();
        generator = new MoveGenerator();
        logger = Logger.getLogger("chessMove");

        String path = "棋谱" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".txt";
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (java.io.IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "不能创建棋谱！").show();
            }
        }
        try {
            FileHandler fileHandler = new FileHandler("棋谱" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".txt");
            fileHandler.setLevel(Level.INFO);
            fileHandler.setFormatter(new MyFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "不能创建棋谱,将在控制台显示！").show();
        }
        s.put(Man.B_PAWN, "");
        s.put(Man.W_PAWN, "");
        s.put(Man.W_ROOK, "R");
        s.put(Man.B_ROOK, "R");
        s.put(Man.W_KNIGHT, "N");
        s.put(Man.B_KNIGHT, "N");
        s.put(Man.W_QUEEN, "Q");
        s.put(Man.B_QUEEN, "Q");
        s.put(Man.B_BISHOP, "B");
        s.put(Man.W_BISHOP, "B");
        s.put(Man.B_KING, "K");
        s.put(Man.W_KING, "K");
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String isWhiteLose() {

        ArrayList<Move> whiteMoves = generator.generateAllMoves(chessBoard, 0);
        ArrayList<Boolean> isMoveSafe = new ArrayList<>();


        boolean isWhiteKingAlive = false;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (chessBoard.get(i, j) == Man.W_KING) {
                    isWhiteKingAlive = true;
                    break;
                }
            }
        if (!isWhiteKingAlive) return "White Lost!";
        else {
            for (Move t : whiteMoves) {
                boolean x = true;
                ChessBoard chessBoard = new ChessBoard();
                for (int row = 0; row < 8; row++) {
                    chessBoard.chessboard[row] = this.chessBoard.chessboard[row].clone();
                }
                chessBoard.execute(t);
                ArrayList<Move> moves = generator.generateAllMoves(chessBoard, 1);
                for (Move movew : moves) {
                    if (this.chessBoard.get(movew.toY, movew.toX) == Man.W_KING) {
                        x = false;
                        break;
                    }
                }
                isMoveSafe.add(x);
            }
            for (boolean t : isMoveSafe) {

                if (t) return "";
            }

            return "White Lost!";
        }

    }

    public String isBlackLose() {
        ArrayList<Move> blackMoves = generator.generateAllMoves(chessBoard, 1);
        ArrayList<Boolean> isMoveSafe = new ArrayList<>();
        boolean isBlackKingAlive = false;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (chessBoard.get(i, j) == Man.B_KING) {
                    isBlackKingAlive = true;
                    break;
                }
            }
        if (!isBlackKingAlive) return "Black Lost!";
        else {
            for (Move t : blackMoves) {
                boolean x = true;
                ChessBoard chessBoard = new ChessBoard();
                for (int row = 0; row < 8; row++) {
                    chessBoard.chessboard[row] = this.chessBoard.chessboard[row].clone();
                }
                chessBoard.execute(t);
                ArrayList<Move> moves = generator.generateAllMoves(chessBoard, 0);
                for (Move movew : moves) {
                    if (this.chessBoard.get(movew.toY, movew.toX) == Man.B_KING) {

                        x = false;
                        break;
                    }
                }
                isMoveSafe.add(x);
            }
            for (boolean t : isMoveSafe) {
                if (t) return "";
            }
            return "Black Lost!";
        }


    }

    public void makeMove() {


        Move move = Zobrist.ItalianStart[chessBoard.hashCode() % Zobrist.size];
        if (move != null) {
            execute(move);
        } else {
            searchMove();

            execute(temp);

        }
    }

    private void searchMove() {
        double score = negaMax(chessBoard, maxDepth, -200000.0, 200000.0);

    }

    private void setEP(Move move) {
        if (chessBoard.get(move.fromY, move.fromX) == Man.B_PAWN && move.toY - move.fromY == 2) {
            generator.isFirstMoveForBlackPawn[move.fromX] = true;
        } else if (chessBoard.get(move.fromY, move.fromX) == Man.W_PAWN && move.toY - move.fromY == -2) {
            generator.isFirstMoveForWhitePawn[move.fromX] = true;
        } else {
            for (int i = 0; i < 8; i++) {
                generator.isFirstMoveForWhitePawn[i] = false;
                generator.isFirstMoveForBlackPawn[i] = false;
            }
        }
    }
    private double negaMax(ChessBoard chessBoard1, int depth, double alpha, double beta) {

        int currentSide = side == 0 ? (maxDepth - depth) % 2 : (maxDepth - depth + 1) % 2;

        if (depth <= 0) {
            return eva.EvaluateW(currentSide, chessBoard1);
        }
        double score;
        ArrayList<Move> moves = generator.generateAllMoves(chessBoard, currentSide);
//        Collections.shuffle(moves);

        for (Move x : moves) {
            ChessBoard newChessBoard = new ChessBoard();
            for (int row = 0; row < 8; row++)
                newChessBoard.chessboard[row] = chessBoard1.chessboard[row].clone();
            newChessBoard.execute(x);
            score = -negaMax(newChessBoard, depth - 1, -beta, -alpha);

            if (score > alpha) {
                alpha = score;
                if (depth == maxDepth) {

                    temp = x;
                }
                if (alpha >= beta) {
                    break;
                }
            }

        }
        return alpha;
    }
    public boolean isValidMove(Move move) {
        return generator.isValidMove(chessBoard, move);
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void addTimes() {
        times++;
    }

    public boolean isBlack(int x) {
        return x > Man.W_QUEEN && x <= Man.B_QUEEN;

    }

    public boolean isWhite(int x) {
        return x < Man.B_PAWN && x > Man.NONE;
    }

    public void execute(Move move) {
        logg(move);
        this.chessBoard.execute(move);
        this.setEP(move);
        int sourceId = chessBoard.get(move.fromY, move.fromX);
        if (sourceId == Man.W_KING) {
            generator.canWhiteShortCastling = false;
            generator.canWhiteLongCastling = false;
        }
        if (sourceId == Man.W_ROOK && move.fromX == 7) generator.canWhiteShortCastling = false;
        if (sourceId == Man.W_ROOK && move.fromX == 0) generator.canWhiteLongCastling = false;
        if (sourceId == Man.B_KING) {
            generator.canBlackShortCastling = false;
            generator.canBlackLongCastling = false;
        }
        if (sourceId == Man.B_ROOK && move.fromX == 7) generator.canBlackShortCastling = false;
        if (sourceId == Man.B_ROOK && move.fromX == 0) generator.canBlackLongCastling = false;

        times++;
    }

    public void logg(Move move) {

        int from = this.chessBoard.get(move.fromY, move.fromX);
        int to = this.chessBoard.get(move.toY, move.toX);
        if (isWhite(from)) {
            builder.append(((int) (times / 2) + 1));
            builder.append(".");
        }

        if (!move.isPromotion && !move.isShortCastling && !move.isLongCastling && !move.isEP) {
            builder.append(s.get(from));
            if (to == 0) {
                builder.append((char) (move.toX + 97));
                builder.append(8 - move.toY);

            } else {
                if (s.get(from).equals(""))
                    builder.append((char) (move.fromX + 97));
                builder.append("x");
                builder.append((char) (move.toX + 97));
                builder.append(8 - move.toY);
            }
        }
        if (move.isPromotion) {
            builder.append((char) (move.toX + 97));
            builder.append(8 - move.toY);
            builder.append("=");
            builder.append(s.get(move.promotionType));
        }
        if (move.isLongCastling) {
            builder.append("0-0-0");
        }
        if (move.isShortCastling) {
            builder.append("0-0");
        }
        if (move.isEP) {
            builder.append((char) (move.fromX + 97));
            builder.append("x");
            builder.append((char) (move.toX + 97));
            builder.append(8 - move.toY);
            builder.append("E.P.");
        }
        builder.append("  ");
        if (isBlack(from)) {
            builder.append("\n");
            logger.info(builder.toString());

            builder.delete(0, builder.length());
        }
    }
}

class EvaluateEngine {
    public HashMap<Integer, Double> WBaseScores = new HashMap<>();
    public int side;
    public double[][] white_pawn_mat =
            {
                    {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                    {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
                    {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
                    {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
                    {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
                    {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
                    {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
                    {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
            };
    public double[][] black_pawn_mat = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
            {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
            {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
            {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
            {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
            {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
    };

    public double[][] knight_mat =
            {
                    {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
                    {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
                    {-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
                    {-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
                    {-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
                    {-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
                    {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
                    {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
            };
    public double[][] white_bishop_mat = {
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
            {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
            {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
            {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
    };
    public double[][] black_bishop_mat = {
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
            {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
            {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
            {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
    };
    public double[][] rook_mat_white = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}
    };
    public double[][] rook_mat_black = {
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}
    };
    public double[][] queen_mat = {
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
    };
    public double[][] king_mat_white = {

            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0},
            {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0}
    };
    public double[][] king_mat_black = {
            {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0},
            {2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0}
    };

    public EvaluateEngine() {

        for (int i = 0; i <= 12; i++) {
            switch (i) {
                case Man.NONE:
                    WBaseScores.put(Man.NONE, 0.0);
                    break;
                case Man.W_PAWN:
                    WBaseScores.put(Man.W_PAWN, 10.0);
                    break;
                case Man.W_KNIGHT:
                    WBaseScores.put(Man.W_KNIGHT, 30.0);
                    break;
                case Man.W_BISHOP:
                    WBaseScores.put(Man.W_BISHOP, 30.0);
                    break;
                case Man.W_ROOK:
                    WBaseScores.put(Man.W_ROOK, 50.0);
                    break;
                case Man.W_QUEEN:
                    WBaseScores.put(Man.W_QUEEN, 90.0);
                    break;
                case Man.W_KING:
                    WBaseScores.put(Man.W_KING, 90000.0);
                    break;
                case Man.B_PAWN:
                    WBaseScores.put(Man.B_PAWN, 10.0);
                    break;
                case Man.B_KNIGHT:
                    WBaseScores.put(Man.B_KNIGHT, 30.0);
                    break;
                case Man.B_BISHOP:
                    WBaseScores.put(Man.B_BISHOP, 30.0);
                    break;
                case Man.B_ROOK:
                    WBaseScores.put(Man.B_ROOK, 50.0);
                    break;
                case Man.B_QUEEN:
                    WBaseScores.put(Man.B_QUEEN, 90.0);
                    break;
                case Man.B_KING:
                    WBaseScores.put(Man.B_KING, 90000.0);
                    break;
                default:
                    break;
            }
        }
        if (side == 1) {
            WBaseScores.replaceAll((s1, s2) -> {
                s2 = -s2;
                return s2;
            });
        }
    }

    public double EvaluateW(int currentSide, ChessBoard chessBoard) {
        double scores = 0;
        double Wscores = 0;
        double Bscores = 0;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                switch (chessBoard.get(i, j)) {
                    case Man.W_PAWN:
                        Wscores += WBaseScores.get(Man.W_PAWN) + (double) white_pawn_mat[i][j];
                        break;
                    case Man.W_ROOK:
                        Wscores += WBaseScores.get(Man.W_ROOK) + (double) rook_mat_white[i][j];
                        break;
                    case Man.W_KNIGHT:
                        Wscores += WBaseScores.get(Man.W_KNIGHT) + (double) knight_mat[i][j];
                        break;
                    case Man.W_BISHOP:
                        Wscores += WBaseScores.get(Man.W_BISHOP) + (double) white_bishop_mat[i][j];
                        break;
                    case Man.W_KING:
                        Wscores += WBaseScores.get(Man.W_KING) + (double) king_mat_white[i][j];
                        break;
                    case Man.W_QUEEN:
                        Wscores += WBaseScores.get(Man.W_QUEEN) + (double) queen_mat[i][j];
                        break;
                    case Man.B_PAWN:
                        Bscores += WBaseScores.get(Man.B_PAWN) + (double) black_pawn_mat[i][j];
                        break;
                    case Man.B_ROOK:
                        Bscores += WBaseScores.get(Man.B_ROOK) + (double) rook_mat_black[i][j];
                        break;
                    case Man.B_KNIGHT:
                        Bscores += WBaseScores.get(Man.B_KNIGHT) + (double) knight_mat[i][j];
                        break;
                    case Man.B_BISHOP:
                        Bscores += WBaseScores.get(Man.B_BISHOP) + (double) black_bishop_mat[i][j];
                        break;
                    case Man.B_KING:
                        Bscores += WBaseScores.get(Man.B_BISHOP) + (double) king_mat_black[i][j];
                        break;
                    case Man.B_QUEEN:
                        Bscores += WBaseScores.get(Man.B_QUEEN) + (double) queen_mat[i][j];
                        break;
                }
            }

        if (currentSide == 0) return -Bscores + Wscores;
        else return -Wscores + Bscores;
    }
}

class MyFormatter extends Formatter {
    public String format(LogRecord record) {
        return formatMessage(record);
    }
}
