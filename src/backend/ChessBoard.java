package backend;

public class ChessBoard {
    int[][] chessboard;
    int hashValue;
    Zobrist zobrist = new Zobrist();
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
        hashValue = zobrist.getHashValue(this);
    }


    public void execute(Move move){
        updateHashCode(move);
        if (!move.isPromotion && !move.isLongCastling && !move.isShortCastling && !move.isEP) {
            chessboard[move.toY][move.toX]=chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX]=0;
        } else if (move.isEP) {
            chessboard[move.toY][move.toX] = chessboard[move.fromY][move.fromX];
            chessboard[move.fromY][move.fromX] = 0;
            if (Man.isWhite(get(move.fromY, move.fromX))) chessboard[move.toY + 1][move.toX] = 0;
            if (Man.isBlack(get(move.fromY, move.fromX))) chessboard[move.toY - 1][move.toX] = 0;
        } else if(move.isPromotion){
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


    public int get(int Y, int X){
        return chessboard[Y][X];

    }

    public int hashCode() {
        return hashValue;
    }

    public void updateHashCode(Move move) {
        int nFromID = this.get(move.fromY, move.fromX);
        int nToID = this.get(move.toY, move.toX);
        if (move.isEP) {
            hashValue ^= zobrist.hashValues[nFromID][move.fromY][move.fromX];
            hashValue ^= zobrist.hashValues[nFromID][move.toY][move.toX];
            if (Man.isWhite(get(move.fromY, move.fromX)))
                hashValue ^= zobrist.hashValues[Man.B_PAWN][move.toY + 1][move.toX];
            if (Man.isBlack(get(move.fromY, move.fromX)))
                hashValue ^= zobrist.hashValues[Man.W_PAWN][move.toY - 1][move.toX];
        } else if (move.isShortCastling || move.isLongCastling) {
            hashValue ^= zobrist.hashValues[nFromID][move.fromY][move.fromX];
            hashValue ^= zobrist.hashValues[nFromID][move.toY][move.toX];
        } else if (move.isPromotion) {
            hashValue ^= zobrist.hashValues[nFromID][move.fromY][move.fromX];
            if (nToID != 0) {
                hashValue ^= zobrist.hashValues[nToID][move.toY][move.toX];
            }
            hashValue ^= zobrist.hashValues[move.promotionType][move.fromY][move.fromX];
        } else {
            hashValue ^= zobrist.hashValues[nFromID][move.fromY][move.fromX];
            if (nToID != 0) {
                hashValue ^= zobrist.hashValues[nToID][move.toY][move.toX];
            }
            hashValue ^= zobrist.hashValues[nFromID][move.toY][move.toX];
        }

    }
}
