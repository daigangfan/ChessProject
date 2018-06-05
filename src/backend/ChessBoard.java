package backend;

public class ChessBoard {
    private int[][] chessboard;

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
                                chessboard[i][j] = Man.B_KING;
                                break;
                            case 4:
                                chessboard[i][j] = Man.B_QUEEN;
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
                            chessboard[i][j] = Man.W_KING;
                            break;
                        case 4:
                            chessboard[i][j] = Man.W_QUEEN;
                            break;
                        default:break;
                    };break;
                    default:break;
                }
            }
    }


    public void execute(Move move){
        if(!move.isPromotion){
            chessboard[move.toY][move.toX]=chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX]=0;

        }
        else if(move.isPromotion){
            chessboard[move.toY][move.toX] = move.promotionType;
            chessboard[move.fromY][move.fromX] = 0;
        }
    }

    public void deExecute(Move move, int nToID) {
        if (!move.isPromotion) {
            chessboard[move.fromY][move.fromX] = chessboard[move.toY][move.toX];
            chessboard[move.toY][move.toX] = nToID;
        } else {
            chessboard[move.toY][move.toX] = nToID;
            chessboard[move.fromY][move.fromX] = move.fromY == 6 ? Man.B_PAWN : Man.W_PAWN;
        }
    }
    public int get(int Y,int X){
        return chessboard[Y][X];

    }
}
