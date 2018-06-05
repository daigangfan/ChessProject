package backend;

public class Move {
    int fromX;
    int fromY;
    int toX;
    int toY;
    boolean isPromotion=false;
    int promotionType=-1;

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
}
