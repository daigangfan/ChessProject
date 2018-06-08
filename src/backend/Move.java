package backend;

public class Move {
    int fromX;
    int fromY;
    int toX;
    int toY;
    boolean isPromotion=false;
    boolean isShortCastling = false;
    boolean isLongCastling = false;
    int promotionType=-1;
    boolean isEP = false;

    public Move(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public void setLongCastling(boolean longCastling) {
        isLongCastling = longCastling;
    }

    public void setShortCastling(boolean shortCastling) {
        isShortCastling = shortCastling;
    }

    public void setEP(boolean EP) {
        isEP = EP;
    }
}
