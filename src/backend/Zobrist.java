package backend;

import java.util.Random;

public class Zobrist {
    static int size = 1024 * 1024;
    static Move[] ItalianStart = new Move[size];
    int[][][] hashValues = new int[13][8][8];

    public Zobrist() {
        Random rand = new Random(1000);
        for (int i = 0; i < 13; i++)
            for (int j = 0; j < 8; j++)
                for (int k = 0; k < 8; k++)
                    hashValues[i][j][k] = rand.nextInt();


    }

    static void initItalian() {
        Move move;
        ChessBoard chessBoard = new ChessBoard();
        move = new Move(4, 6, 4, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(4, 1, 4, 3);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(6, 7, 5, 5);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(1, 0, 2, 2);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(5, 7, 2, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(5, 0, 2, 3);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 6, 2, 5);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(6, 0, 5, 2);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(3, 6, 3, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(4, 3, 3, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 5, 3, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 3, 1, 4);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 7, 3, 6);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(1, 4, 3, 6);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(1, 7, 3, 6);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(3, 1, 3, 3);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(4, 4, 3, 3);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(5, 2, 3, 3);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(7, 3, 1, 5);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 2, 4, 1);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(4, 7, 6, 7);
        move.isShortCastling = true;
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(4, 0, 6, 0);
        move.isShortCastling = true;
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(5, 7, 4, 7);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        move = new Move(2, 1, 2, 2);
        ItalianStart[chessBoard.hashCode() % size] = move;
        chessBoard.execute(move);
        

    }

    int getHashValue(ChessBoard chessBoard) {
        int hashKey = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                hashKey ^= hashValues[chessBoard.get(i, j)][i][j];
            }
        return hashKey;
    }
}
