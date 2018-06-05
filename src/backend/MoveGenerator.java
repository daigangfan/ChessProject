package backend;

import java.util.ArrayList;
public class MoveGenerator {
    int[][]Bthreaten=new int[8][8];
    int[][]Wthreaten=new int[8][8];
    private static int[] types = {Man.W_QUEEN, Man.W_BISHOP, Man.W_KNIGHT, Man.W_ROOK};
    public static void main(String[] args) {
        // unit test
        ChessBoard chessBoard = new ChessBoard();
        MoveGenerator g = new MoveGenerator();
        g.updateThreatenMatrix(chessBoard);
        Move move = new Move(1, 1, 1, 2);
        System.out.println(g.isValidMove(chessBoard, move));
        chessBoard.execute(move);
        ArrayList<Move> x = g.generateAllMoves(chessBoard, 0);
        System.out.println(x.size());

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Integer.toString(chessBoard.get(i, j)) + " ");
            }
            System.out.println("");
        }
        System.out.println("");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Integer.toString(g.Wthreaten[i][j]) + " ");

            }
            System.out.println("");
        }
        System.out.println("");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Integer.toString(g.Bthreaten[i][j]) + " ");

            }
            System.out.println("");
        }
    }

    private void updateThreatenMatrix(ChessBoard chessBoard){
        //TODO: check the subscription.
        int x,y;
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){
            Bthreaten[i][j]=0;
            Wthreaten[i][j]=0;
            }
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){

            switch (chessBoard.get(i,j)){
                case Man.B_PAWN:
                    if (i + 1 < 8 && j + 1 < 8) if (chessBoard.get(i + 1, j + 1) == Man.NONE) Wthreaten[i + 1][j + 1]++;
                    if (j - 1 >= 0 && i + 1 < 8)
                        if (chessBoard.get(i + 1, j - 1) == Man.NONE) Wthreaten[i + 1][j - 1]++;
                    break;
                case Man.B_ROOK:
                    for (int t = i + 1; t < 8 && chessBoard.get(t, j) == Man.NONE; t++) Wthreaten[t][j]++;
                    for (int t = i - 1; t >= 0 && chessBoard.get(t, j) == Man.NONE; t--) Wthreaten[t][j]++;
                    for (int t = j + 1; t < 8 && chessBoard.get(i, t) == Man.NONE; t++) Wthreaten[i][t]++;
                    for (int t = j - 1; t > 0 && chessBoard.get(i, t) == Man.NONE; t--) Wthreaten[i][t]--;
                    break;
                case Man.B_BISHOP:
                    x=i+1;
                    y=j+1;
                    while (x < 8 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while (x >= 0 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while (x >= 0 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while (x < 8 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case Man.B_KNIGHT:
                    if (i >= 1 && j >= 2 && chessBoard.get(i - 1, j - 2) == Man.NONE)
                        Wthreaten[i-1][j-2]++;
                    if (i < 7 && j >= 2 && chessBoard.get(i + 1, j - 2) == Man.NONE)
                        Wthreaten[i+1][j-2]++;
                    if (i < 7 && j < 6 && chessBoard.get(i + 1, j + 2) == Man.NONE)
                        Wthreaten[i+1][j+2]++;
                    if (i >= 1 && j < 6 && chessBoard.get(i - 1, j + 2) == Man.NONE)
                        Wthreaten[i-1][j+2]++;
                    if (i >= 2 && j >= 1 && chessBoard.get(i - 2, j - 1) == Man.NONE)
                        Wthreaten[i-2][j-1]++;
                    if (i < 6 && j >= 1 && chessBoard.get(i + 2, j - 1) == Man.NONE)
                        Wthreaten[i+2][j-1]++;
                    if (i >= 2 && j < 7 && chessBoard.get(i - 2, j + 1) == Man.NONE)
                        Wthreaten[i-2][j+1]++;
                    if (i < 6 && j < 7 && chessBoard.get(i + 2, j + 1) == Man.NONE)
                        Wthreaten[i+2][j+1]++;
                    break;
                case Man.B_KING:
                    if (i >= 1 && j >= 1 && chessBoard.get(i - 1, j - 1) == Man.NONE)
                        Wthreaten[i-1][j-1]++;
                    if (i < 7 && j >= 1 && chessBoard.get(i + 1, j - 1) == Man.NONE)
                        Wthreaten[i+1][j-1]++;
                    if (i >= 1 && j < 7 && chessBoard.get(i - 1, j + 1) == Man.NONE)
                        Wthreaten[i-1][j+1]++;
                    if (i >= 1 && j >= 1 && chessBoard.get(i - 1, j - 1) == Man.NONE)
                        Wthreaten[i+1][j+1]++;
                    if (i >= 1 && chessBoard.get(i - 1, j) == Man.NONE)
                        Wthreaten[i-1][j]++;
                    if (i < 7 && chessBoard.get(i + 1, j) == Man.NONE)
                        Wthreaten[i+1][j]++;
                    if (j < 7 && chessBoard.get(i, j + 1) == Man.NONE)
                        Wthreaten[i][j+1]++;
                    if (j >= 1 && chessBoard.get(i, j - 1) == Man.NONE)
                        Wthreaten[i][j-1]++;
                    break;
                case Man.B_QUEEN:
                    for (int t = i + 1; t < 8 && chessBoard.get(t, j) == Man.NONE; t++) Wthreaten[t][j]++;
                    for (int t = i - 1; t >= 0 && chessBoard.get(t, j) == Man.NONE; t--) Wthreaten[t][j]++;
                    for (int t = j + 1; t < 8 && chessBoard.get(i, t) == Man.NONE; t++) Wthreaten[i][t]++;
                    for (int t = j - 1; t > 0 && chessBoard.get(i, t) == Man.NONE; t--) Wthreaten[i][t]--;
                    x=i+1;
                    y=j+1;
                    while (x < 8 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while (x >= 0 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while (x >= 0 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while (x < 8 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Wthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case Man.W_PAWN:
                    if (i - 1 >= 0 && j + 1 < 8)
                        if (chessBoard.get(i - 1, j + 1) == Man.NONE) Bthreaten[i - 1][j + 1]++;
                    if (i - 1 >= 0 && j - 1 >= 0)
                        if (chessBoard.get(i - 1, j - 1) == Man.NONE) Bthreaten[i - 1][j - 1]++;
                    break;
                case Man.W_ROOK:
                    for (int t = i + 1; t < 8 && chessBoard.get(t, j) == Man.NONE; t++) Bthreaten[t][j]++;
                    for (int t = i - 1; t >= 0 && chessBoard.get(t, j) == Man.NONE; t--) Bthreaten[t][j]++;
                    for (int t = j + 1; t < 8 && chessBoard.get(i, t) == Man.NONE; t++) Bthreaten[i][t]++;
                    for (int t = j - 1; t > 0 && chessBoard.get(i, t) == Man.NONE; t--) Bthreaten[i][t]--;
                    break;
                case Man.W_BISHOP:
                    x=i+1;
                    y=j+1;
                    while (x < 8 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while (x >= 0 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while (x >= 0 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while (x < 8 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case Man.W_KNIGHT:
                    if (i >= 1 && j >= 2 && chessBoard.get(i - 1, j - 2) == Man.NONE)
                        Bthreaten[i-1][j-2]++;
                    if (i < 7 && j >= 2 && chessBoard.get(i + 1, j - 2) == Man.NONE)
                        Bthreaten[i+1][j-2]++;
                    if (i < 7 && j < 6 && chessBoard.get(i + 1, j + 2) == Man.NONE)
                        Bthreaten[i+1][j+2]++;
                    if (i >= 1 && j < 6 && chessBoard.get(i - 1, j + 2) == Man.NONE)
                        Bthreaten[i-1][j+2]++;
                    if (i >= 2 && j >= 1 && chessBoard.get(i - 2, j - 1) == Man.NONE)
                        Bthreaten[i-2][j-1]++;
                    if (i < 6 && j >= 1 && chessBoard.get(i + 2, j - 1) == Man.NONE)
                        Bthreaten[i+2][j-1]++;
                    if (i < 6 && j < 7 && chessBoard.get(i + 2, j + 1) == Man.NONE)
                        Bthreaten[i+2][j+1]++;
                    if (i >= 2 && j < 7 && chessBoard.get(i - 2, j + 1) == Man.NONE)
                        Bthreaten[i-2][j+1]++;
                    break;
                case Man.W_KING:
                    if (i >= 1 && j >= 1 && chessBoard.get(i - 1, j - 1) == Man.NONE)
                        Bthreaten[i-1][j-1]++;
                    if (i < 7 && j >= 1 && chessBoard.get(i + 1, j - 1) == Man.NONE)
                        Bthreaten[i+1][j-1]++;
                    if (i >= 1 && j < 7 && chessBoard.get(i - 1, j + 1) == Man.NONE)
                        Bthreaten[i-1][j+1]++;
                    if (i < 7 && j < 7 && chessBoard.get(i - 1, j - 1) == Man.NONE)
                        Bthreaten[i+1][j+1]++;
                    if (i >= 1 && chessBoard.get(i - 1, j) == Man.NONE)
                        Bthreaten[i-1][j]++;
                    if (i < 7 && chessBoard.get(i + 1, j) == Man.NONE)
                        Bthreaten[i+1][j]++;
                    if (j < 7 && chessBoard.get(i, j + 1) == Man.NONE)
                        Bthreaten[i][j+1]++;
                    if (j >= 1 && chessBoard.get(i, j - 1) == Man.NONE)
                        Bthreaten[i][j-1]++;
                    break;
                case Man.W_QUEEN:
                    for (int t = i + 1; t < 8 && chessBoard.get(t, j) == Man.NONE; t++) Bthreaten[t][j]++;
                    for (int t = i - 1; t >= 0 && chessBoard.get(t, j) == Man.NONE; t--) Bthreaten[t][j]++;
                    for (int t = j + 1; t < 8 && chessBoard.get(i, t) == Man.NONE; t++) Bthreaten[i][t]++;
                    for (int t = j - 1; t > 0 && chessBoard.get(i, t) == Man.NONE; t--) Bthreaten[i][t]--;
                    x=i+1;
                    y=j+1;
                    while (x < 8 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while (x >= 0 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while (x >= 0 && y < 8 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while (x < 8 && y >= 0 && chessBoard.get(x, y) == Man.NONE) {
                        Bthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                default:break;
            }
            }
    }

    boolean isValidMove(ChessBoard chessBoard, Move move) {
        int i, j;
        updateThreatenMatrix(chessBoard);
        int nMoveID = chessBoard.get(move.fromY, move.fromX);
        int nTargetID = chessBoard.get(move.toY, move.toX);
        if (Man.isSameSide(nMoveID, nTargetID)) return false;
        switch (nMoveID) {
            case Man.B_PAWN:
                if (move.fromY != 6) {
                    if (move.fromY == 1 && move.toY - move.fromY == 2 && nTargetID == Man.NONE && move.toX == move.fromX)
                        return true;
                    if (move.toY - move.fromY == 1 && move.toX == move.fromX && nTargetID == Man.NONE) return true;
                    if (move.toY - move.fromY == 1 && Math.abs(move.toX - move.fromX) == 1 && nTargetID != Man.NONE)
                        return true;
                } else {
                    if (move.isPromotion) {
                        if (move.toY - move.fromY == 1 && move.toX == move.fromX && nTargetID == Man.NONE) return true;
                        if (move.toY - move.fromY == 1 && Math.abs(move.toX - move.fromX) == 1 && nTargetID != Man.NONE)
                            return true;
                    }
                }
                break;
            case Man.W_PAWN:
                if (move.fromY != 1) {
                    if (move.fromY == 6 && move.toY - move.fromY == -2 && nTargetID == Man.NONE && move.toX == move.fromX)
                    return true;
                    if (move.toY - move.fromY == -1 && move.toX == move.fromX && nTargetID == Man.NONE)
                    return true;
                    if (move.toY - move.fromY == -1 && Math.abs(move.toX - move.fromX) == 1 && nTargetID != Man.NONE)
                    return true;
                } else {
                    if (move.isPromotion) {
                        if (move.toY - move.fromY == -1 && move.toX == move.fromX && nTargetID == Man.NONE) return true;
                        if (move.toY - move.fromY == -1 && Math.abs(move.toX - move.fromX) == 1 && nTargetID != Man.NONE)
                            return true;
                    }
                }
                break;
            case Man.B_ROOK:
            case Man.W_ROOK:
                if (move.fromX == move.toX) {
                    int up = Math.min(move.fromY, move.toY);
                    int down = Math.max(move.fromY, move.toY);
                    for (i = up + 1; i < down; i++) {
                        if (chessBoard.get(i, move.fromX) != Man.NONE) return false;
                    }
                    return true;
                }
                if (move.fromY == move.toY) {
                    int left = Math.min(move.fromX, move.toX);
                    int right = Math.max(move.fromX, move.toX);
                    for (i = left + 1; i < right; i++) {
                        if (chessBoard.get(move.fromY, i) != Man.NONE) return false;
                    }
                    return true;
                }
                break;
            case Man.B_BISHOP:
            case Man.W_BISHOP:
                if (Math.abs(move.fromX - move.toX) == Math.abs(move.fromY - move.toY)) {
                    if ((move.fromX - move.toX) * (move.fromY - move.toY) > 0) {
                        i = Math.min(move.fromX, move.toX) + 1;
                        j = Math.min(move.fromY, move.toY) + 1;
                        while (i < Math.max(move.fromX, move.toX) && j < Math.max(move.fromY, move.toY)) {
                            if (chessBoard.get(j, i) != Man.NONE) return false;
                            j++;
                            i++;
                        }
                        return true;
                    }
                    if ((move.fromX - move.toX) * (move.fromY - move.toY) < 0) {
                        i = Math.min(move.fromX, move.toX) + 1;
                        j = Math.max(move.fromY, move.toY) - 1;
                        while (i < Math.max(move.fromX, move.toX) && j > Math.min(move.fromY, move.toY)) {
                            if (chessBoard.get(j, i) != Man.NONE) return false;
                            j--;
                            i++;
                        }
                        return true;
                    }
                }
                ;
                break;
            case Man.B_KNIGHT:
            case Man.W_KNIGHT:
                if ((Math.abs(move.toX - move.fromX) == 1 && Math.abs(move.toY - move.fromY) == 2) || ((Math.abs(move.toX - move.fromX) == 2 && Math.abs(move.toY - move.fromY) == 1)))
                    return true;
                break;
            case Man.B_QUEEN:
            case Man.W_QUEEN:
                if (move.fromX == move.toX) {
                    int up = Math.min(move.fromY, move.toY);
                    int down = Math.max(move.fromY, move.toY);
                    for (i = up + 1; i < down; i++) {
                        if (chessBoard.get(i, move.fromX) != Man.NONE) return false;
                    }
                    return true;
                }
                if (move.fromY == move.toY) {
                    int left = Math.min(move.fromX, move.toX);
                    int right = Math.max(move.fromX, move.toX);
                    for (i = left + 1; i < right; i++) {
                        if (chessBoard.get(move.fromY, i) != Man.NONE) return false;
                    }
                    return true;
                }
                if (Math.abs(move.fromX - move.toX) == Math.abs(move.fromY - move.toY)) {
                    if ((move.fromX - move.toX) * (move.fromY - move.toY) > 0) {
                        i = Math.min(move.fromX, move.toX) + 1;
                        j = Math.min(move.fromY, move.toY) + 1;
                        while (i < Math.max(move.fromX, move.toX) && j < Math.max(move.fromY, move.toY)) {
                            if (chessBoard.get(j, i) != Man.NONE) return false;
                            j++;
                            i++;
                        }
                        return true;
                    }
                    if ((move.fromX - move.toX) * (move.fromY - move.toY) < 0) {
                        i = Math.min(move.fromX, move.toX) + 1;
                        j = Math.max(move.fromY, move.toY) - 1;
                        while (i < Math.max(move.fromX, move.toX) && j > Math.min(move.fromY, move.toY)) {
                            if (chessBoard.get(j, i) != Man.NONE) return false;
                            j--;
                            i++;
                        }
                        return true;
                    }
                }
                ;
                break;
            case Man.B_KING:
                if (Math.abs(move.toX - move.fromX) <= 1 && Math.abs(move.toY - move.fromY) <= 1 && Bthreaten[move.toY][move.toX] == 0)
                    return true;
                break;
            case Man.W_KING:
                if (Math.abs(move.toX - move.fromX) <= 1 && Math.abs(move.toY - move.fromY) <= 1 && Wthreaten[move.toY][move.toX] == 0)
                    return true;
                break;
            default:
                break;
        }
        return false;
    }

    public ArrayList<Move> generateAllMoves(ChessBoard chessBoard, int side) {
        //TODO: check the efficiency for direct search
        ArrayList<Move> moves = new ArrayList<>();

        for (int fromX = 0; fromX < 8; fromX++)
            for (int fromY = 0; fromY < 8; fromY++) {
                for (int toY = 0; toY < 8; toY++)
                    for (int toX = 0; toX < 8; toX++) {
                        if (Man.isWhite(chessBoard.get(fromY, fromX)) && side == 0) {
                            Move move = new Move(fromX, fromY, toX, toY);
                            if (fromY == 1 && chessBoard.get(fromY, fromX) == Man.W_PAWN) {

                                move.isPromotion = true;
                                if (isValidMove(chessBoard, move)) {
                                    for (int type : types) {
                                        move.promotionType = type;
                                        moves.add(move);

                                    }
                                    continue;
                                } else move.isPromotion = false;
                            }
                            if (isValidMove(chessBoard, move)) {
                                moves.add(move);
                            }
                        }
                        if (Man.isBlack(chessBoard.get(fromY, fromX)) && side == 1) {
                            Move move = new Move(fromX, fromY, toX, toY);
                            if (fromY == 6 && chessBoard.get(fromY, fromX) == Man.B_PAWN) {
                                move.isPromotion = true;
                                if (isValidMove(chessBoard, move)) {
                                    for (int type : types) {
                                        move.promotionType = type;
                                        moves.add(move);
                                    }
                                    continue;
                                } else move.isPromotion = false;
                            }
                            if (isValidMove(chessBoard, move)) {
                                moves.add(move);
                            }
                        }
                    }
            }

        return moves;
    }
}
