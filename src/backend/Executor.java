package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class Executor {
    int times = 0;
    int side = 0;//电脑方向0白1黑
    int maxDepth = 4;
    ChessBoard chessBoard;
    MoveGenerator generator;
    Move temp;

    EvaluateEngine eva = new EvaluateEngine();
    public Executor() {
        chessBoard = new ChessBoard();
        generator = new MoveGenerator();
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String isWhiteLose() {
        ArrayList<Move> blackMoves = generator.generateAllMoves(chessBoard, 1);
        ArrayList<Move> whiteMoves = generator.generateAllMoves(chessBoard, 0);
        ArrayList<Move> blackThreatenMoves = new ArrayList<>();
        Move move;
        boolean isWhiteSafe = false;
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
            for (Move e : blackMoves) {
                if (chessBoard.get(e.toY, e.toX) == Man.W_KING) {
                    blackThreatenMoves.add(e);
                }

            }
            if (blackThreatenMoves.size() == 0) return "";
            else if (blackThreatenMoves.size() == 1) {
                if (generator.Bthreaten[blackThreatenMoves.get(0).fromY][blackThreatenMoves.get(0).fromX] > 0)
                    return "";
                else {
                    for (Move t : whiteMoves) {
                        if (chessBoard.get(t.fromY, t.fromX) == Man.W_KING) {
                            isWhiteSafe = true;
                        }
                    }
                    if (!isWhiteSafe) return "White Lost!";
                }
            } else {
                for (Move t : whiteMoves) {
                    if (chessBoard.get(t.fromY, t.fromX) == Man.W_KING) {
                        isWhiteSafe = true;
                    }
                }
                if (!isWhiteSafe) return "White Lost!";
            }
        }

        return "";
    }

    public String isBlackLose() {
        ArrayList<Move> blackMoves = generator.generateAllMoves(chessBoard, 1);
        ArrayList<Move> whiteMoves = generator.generateAllMoves(chessBoard, 0);
        ArrayList<Move> whiteThreatenMoves = new ArrayList<>();
        Move move;
        boolean isBlackSafe = false;
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
            for (Move e : whiteMoves) {
                if (chessBoard.get(e.toY, e.toX) == Man.B_KING) {
                    whiteThreatenMoves.add(e);
                }

            }
            if (whiteThreatenMoves.size() == 0) return "";
            else if (whiteThreatenMoves.size() == 1) {
                if (generator.Bthreaten[whiteThreatenMoves.get(0).fromY][whiteThreatenMoves.get(0).fromX] > 0)
                    return "";
                else {
                    for (Move t : blackMoves) {
                        if (chessBoard.get(t.fromY, t.fromX) == Man.B_KING) {
                            isBlackSafe = true;
                        }
                    }
                    if (!isBlackSafe) return "Black Lost!";
                }
            } else {
                for (Move t : blackMoves) {
                    if (chessBoard.get(t.fromY, t.fromX) == Man.B_KING) {
                        isBlackSafe = true;
                    }
                }
                if (!isBlackSafe) return "Black Lost!";
            }
        }

        return "";
    }

    public void makeMove() {
        //意大利开局
        Move move;
        if (side == 0) {
            switch (times) {
                case 0:
                    move = new Move(4, 6, 4, 4);
                    chessBoard.execute(move);
                    times++;
                    break;
                case 2:
                    move = new Move(6, 7, 5, 5);
                    chessBoard.execute(move);
                    times++;
                    break;
                case 4:
                    move = new Move(5, 7, 2, 4);
                    chessBoard.execute(move);
                    times++;
                    break;
                default:
                    break;
            }
            if (times > 5) {
                searchMove();
                chessBoard.execute(temp);
                times++;
            }
        }
        if (side == 1) {
            switch (times) {
                case 1:
                    move = new Move(4, 1, 4, 3);
                    chessBoard.execute(move);
                    times++;
                    break;
                case 3:
                    move = new Move(1, 0, 2, 2);
                    chessBoard.execute(move);
                    times++;
                    break;
                case 5:

                    move = new Move(5, 0, 2, 3);
                    chessBoard.execute(move);
                    times++;
                    break;
                default:
                    break;
            }
            if (times > 6) {
                searchMove();
                chessBoard.execute(temp);
                times++;
            }
        }

    }

    private void searchMove() {
        double score = negaMax(chessBoard, maxDepth, -20000.0, 20000.0);

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
        this.chessBoard.execute(move);
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
                    WBaseScores.put(Man.W_KING, 9000.0);
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
                    WBaseScores.put(Man.B_KING, 9000.0);
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
