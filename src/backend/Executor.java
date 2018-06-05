package backend;

public class Executor {
    int times = 0;
    int side = 0;//电脑方向0白1黑
    ChessBoard chessBoard;
    MoveGenerator generator;

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
        }

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
        return x > man.W_QUEEN && x <= man.B_QUEEN;

    }

    public boolean isWhite(int x) {
        return x < man.B_PAWN && x > man.NONE;
    }

    public void execute(Move move) {
        this.chessBoard.execute(move);
        times++;
    }
}
