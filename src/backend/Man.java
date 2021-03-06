package backend;

public class Man {
    /*棋子管理类
    * */
    public static final int NONE=0;
    public static final int W_PAWN=1;
    public static final int W_ROOK=2;
    public static final int W_KNIGHT=3;
    public static final int W_BISHOP=4;
    public static final int W_KING=5;
    public static final int W_QUEEN=6;
    public static final int B_PAWN=7;
    public static final int B_ROOK=8;
    public static final int B_KNIGHT=9;
    public static final int B_BISHOP=10;
    public static final int B_KING=11;
    public static final int B_QUEEN=12;
    public static boolean isSameSide(int moveID,int targetID){//是否同一边
        return (isBlack(moveID) && isBlack(targetID)) || (isWhite(moveID) && isWhite(targetID));
    }

    public static boolean isBlack(int x) {
        return x > Man.W_QUEEN && x <= Man.B_QUEEN;

    }

    public static boolean isWhite(int x) {
        return x < Man.B_PAWN && x > Man.NONE;
    }

}
