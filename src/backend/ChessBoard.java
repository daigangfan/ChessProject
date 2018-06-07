package backend;

public class ChessBoard {
    int[][] chessboard;

    //电脑方向，0白1黑
    public ChessBoard(){
        chessboard=new int[8][8];
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){
            chessboard[i][j]=0;
                switch (i){
                    case 0:
                        switch (j){
                            case 0:
                            case 7:
                                chessboard[i][j] = Man.B_ROOK;
                                break;
                            case 1:
                            case 6:
                                chessboard[i][j] = Man.B_KNIGHT;
                                break;
                            case 2:
                            case 5:
                                chessboard[i][j] = Man.B_BISHOP;
                                break;
                            case 3:
                                chessboard[i][j] = Man.B_QUEEN;
                                break;
                            case 4:
                                chessboard[i][j] = Man.B_KING;
                                break;
                            default:break;
                        };
                        break;

                    case 1:
                        chessboard[i][j] = Man.B_PAWN;
                        break;
                    case 6:
                        chessboard[i][j] = Man.W_PAWN;
                        break;
                    case 7:switch (j){
                        case 0:
                        case 7:
                            chessboard[i][j] = Man.W_ROOK;
                            break;
                        case 1:
                        case 6:
                            chessboard[i][j] = Man.W_KNIGHT;
                            break;
                        case 2:
                        case 5:
                            chessboard[i][j] = Man.W_BISHOP;
                            break;
                        case 3:
                            chessboard[i][j] = Man.W_QUEEN;
                            break;
                        case 4:
                            chessboard[i][j] = Man.W_KING;
                            break;
                        default:break;
                    };break;
                    default:break;
                }
            }
    }


    public void execute(Move move){
        if (!move.isPromotion && !move.isLongCastling && !move.isShortCastling) {
            chessboard[move.toY][move.toX]=chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX]=0;

        }
        else if(move.isPromotion){
            chessboard[move.toY][move.toX] = move.promotionType;
            chessboard[move.fromY][move.fromX] = 0;
        } else if (move.isLongCastling) {
            chessboard[move.toY][move.toX] = chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX] = 0;
            if (move.toY == 0) this.execute(new Move(0, 0, 3, 0));
            if (move.toY == 7) this.execute(new Move(0, 7, 3, 7));
        } else if (move.isShortCastling) {
            chessboard[move.toY][move.toX] = chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX] = 0;
            if (move.toY == 0) this.execute(new Move(7, 0, 5, 0));
            if (move.toY == 7) this.execute(new Move(7, 7, 5, 7));
        }
    }

    public void deExecute(Move move, int nToID) {
        if (!move.isPromotion && !move.isLongCastling && !move.isShortCastling) {
            chessboard[move.fromY][move.fromX] = chessboard[move.toY][move.toX];
            chessboard[move.toY][move.toX] = nToID;
        } else {
            if (move.isPromotion) {
                chessboard[move.toY][move.toX] = nToID;
                chessboard[move.fromY][move.fromX] = move.fromY == 6 ? Man.B_PAWN : Man.W_PAWN;
            }
            if (move.isLongCastling) {
                chessboard[move.fromY][move.fromX] = chessboard[move.toY][move.toX];
                if (move.toY == 0) {
                    this.deExecute(new Move(0, 0, 3, 0), Man.NONE);
                }
                if (move.toY == 7) {
                    this.deExecute(new Move(0, 7, 3, 7), Man.NONE);
                }
            }
            if (move.isShortCastling) {
                chessboard[move.fromY][move.fromX] = chessboard[move.toY][move.toX];
                if (move.toY == 0) {
                    this.deExecute(new Move(7, 0, 5, 0), Man.NONE);
                }
                if (move.toY == 7) {
                    this.deExecute(new Move(7, 7, 5, 7), Man.NONE);
                }
            }
        }

    }
    public int get(int Y,int X){
        return chessboard[Y][X];

    }
}
