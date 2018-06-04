package backend;

public class MoveGenerator {
    int[][]Bthreaten=new int[8][8];
    int[][]Wthreaten=new int[8][8];
    private void updateThreatenMatrix(ChessBoard chessBoard){
        int x,y;
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){
            Bthreaten[i][j]=0;
            Wthreaten[i][j]=0;
            }
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++){

            switch (chessBoard.get(i,j)){
                case man.B_PAWN:
                    if(i+1<8 && j+1<8)if(chessBoard.get(i+1,j+1)==man.NONE) Wthreaten[i+1][j+1]++;
                    if(j-1>=0 && i+1<8) if(chessBoard.get(i+1,j-1)==man.NONE) Wthreaten[i+1][j-1]++;
                    break;
                case man.B_ROOK:
                    for(int t=i+1;t<8&&chessBoard.get(t,j)==man.NONE;t++) Wthreaten[t][j]++;
                    for(int t=i-1;t>=0&&chessBoard.get(t,j)==man.NONE;t--) Wthreaten[t][j]++;
                    for(int t=j+1;t<8&&chessBoard.get(i,t)==man.NONE;t++) Wthreaten[i][t]++;
                    for(int t=j-1;t>0&&chessBoard.get(i,t)==man.NONE;t--) Wthreaten[i][t]--;
                    break;
                case man.B_BISHOP:
                    x=i+1;
                    y=j+1;
                    while(x<8&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while(x>=0&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while(x>=0&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while(x<8&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case man.B_KNIGHT:
                    if(i>=1&&j>=2&&chessBoard.get(i-1,j-2)==man.NONE)
                        Wthreaten[i-1][j-2]++;
                    if(i<7&&j>=2&&chessBoard.get(i+1,j-2)==man.NONE)
                        Wthreaten[i+1][j-2]++;
                    if(i<7&&j<6&&chessBoard.get(i+1,j+2)==man.NONE)
                        Wthreaten[i+1][j+2]++;
                    if(i>=1&&j<6&&chessBoard.get(i-1,j+2)==man.NONE)
                        Wthreaten[i-1][j+2]++;
                    if(i>=2&&j>=1&&chessBoard.get(i-2,j-1)==man.NONE)
                        Wthreaten[i-2][j-1]++;
                    if(i<6&&j>=1&&chessBoard.get(i+2,j-1)==man.NONE)
                        Wthreaten[i+2][j-1]++;
                    if(i>=2&&j<7&&chessBoard.get(i-2,j+1)==man.NONE)
                        Wthreaten[i-2][j+1]++;
                    if(i<6&&j<7&&chessBoard.get(i+2,j+1)==man.NONE)
                        Wthreaten[i+2][j+1]++;
                    break;
                case man.B_KING:
                    if(i>=1&&j>=1&&chessBoard.get(i-1,j-1)==man.NONE)
                        Wthreaten[i-1][j-1]++;
                    if(i<7&&j>=1&&chessBoard.get(i+1,j-1)==man.NONE)
                        Wthreaten[i+1][j-1]++;
                    if(i>=1&&j<7&&chessBoard.get(i-1,j+1)==man.NONE)
                        Wthreaten[i-1][j+1]++;
                    if(i>=1&&j>=1&&chessBoard.get(i-1,j-1)==man.NONE)
                        Wthreaten[i+1][j+1]++;
                    if(i>=1&&chessBoard.get(i-1,j)==man.NONE)
                        Wthreaten[i-1][j]++;
                    if(i<7&&chessBoard.get(i+1,j)==man.NONE)
                        Wthreaten[i+1][j]++;
                    if(j<7&&chessBoard.get(i,j+1)==man.NONE)
                        Wthreaten[i][j+1]++;
                    if(j>=1&&chessBoard.get(i,j-1)==man.NONE)
                        Wthreaten[i][j-1]++;
                    break;
                case man.B_QUEEN:
                    for(int t=i+1;t<8&&chessBoard.get(t,j)==man.NONE;t++) Wthreaten[t][j]++;
                    for(int t=i-1;t>=0&&chessBoard.get(t,j)==man.NONE;t--) Wthreaten[t][j]++;
                    for(int t=j+1;t<8&&chessBoard.get(i,t)==man.NONE;t++) Wthreaten[i][t]++;
                    for(int t=j-1;t>0&&chessBoard.get(i,t)==man.NONE;t--) Wthreaten[i][t]--;
                    x=i+1;
                    y=j+1;
                    while(x<8&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while(x>=0&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while(x>=0&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while(x<8&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Wthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case man.W_PAWN:
                    if(i-1>=0 && j+1<8)if(chessBoard.get(i-1,j+1)==man.NONE) Bthreaten[i-1][j+1]++;
                    if(i-1>=0 && j-1>=0) if(chessBoard.get(i-1,j-1)==man.NONE) Bthreaten[i-1][j-1]++;
                    break;
                case man.W_ROOK:
                    for(int t=i+1;t<8&&chessBoard.get(t,j)==man.NONE;t++) Bthreaten[t][j]++;
                    for(int t=i-1;t>=0&&chessBoard.get(t,j)==man.NONE;t--) Bthreaten[t][j]++;
                    for(int t=j+1;t<8&&chessBoard.get(i,t)==man.NONE;t++) Bthreaten[i][t]++;
                    for(int t=j-1;t>0&&chessBoard.get(i,t)==man.NONE;t--) Bthreaten[i][t]--;
                    break;
                case man.W_BISHOP:
                    x=i+1;
                    y=j+1;
                    while(x<8&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while(x>=0&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while(x>=0&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while(x<8&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                case man.W_KNIGHT:
                    if(i>=1&&j>=2&&chessBoard.get(i-1,j-2)==man.NONE)
                        Bthreaten[i-1][j-2]++;
                    if(i<7&&j>=2&&chessBoard.get(i+1,j-2)==man.NONE)
                        Bthreaten[i+1][j-2]++;
                    if(i<7&&j<6&&chessBoard.get(i+1,j+2)==man.NONE)
                        Bthreaten[i+1][j+2]++;
                    if(i>=1&&j<6&&chessBoard.get(i-1,j+2)==man.NONE)
                        Bthreaten[i-1][j+2]++;
                    if(i>=2&&j>=1&&chessBoard.get(i-2,j-1)==man.NONE)
                        Bthreaten[i-2][j-1]++;
                    if(i<6&&j>=1&&chessBoard.get(i+2,j-1)==man.NONE)
                        Bthreaten[i+2][j-1]++;
                    if(i<6&&j<7&&chessBoard.get(i+2,j+1)==man.NONE)
                        Bthreaten[i+2][j+1]++;
                    if(i>=2&&j<7&&chessBoard.get(i-2,j+1)==man.NONE)
                        Bthreaten[i-2][j+1]++;
                    break;
                case man.W_KING:
                    if(i>=1&&j>=1&&chessBoard.get(i-1,j-1)==man.NONE)
                        Bthreaten[i-1][j-1]++;
                    if(i<7&&j>=1&&chessBoard.get(i+1,j-1)==man.NONE)
                        Bthreaten[i+1][j-1]++;
                    if(i>=1&&j<7&&chessBoard.get(i-1,j+1)==man.NONE)
                        Bthreaten[i-1][j+1]++;
                    if(i<7&&j<7&&chessBoard.get(i-1,j-1)==man.NONE)
                        Bthreaten[i+1][j+1]++;
                    if(i>=1&&chessBoard.get(i-1,j)==man.NONE)
                        Bthreaten[i-1][j]++;
                    if(i<7&&chessBoard.get(i+1,j)==man.NONE)
                        Bthreaten[i+1][j]++;
                    if(j<7&&chessBoard.get(i,j+1)==man.NONE)
                        Bthreaten[i][j+1]++;
                    if(j>=1&&chessBoard.get(i,j-1)==man.NONE)
                        Bthreaten[i][j-1]++;
                    break;
                case man.W_QUEEN:
                    for(int t=i+1;t<8&&chessBoard.get(t,j)==man.NONE;t++) Bthreaten[t][j]++;
                    for(int t=i-1;t>=0&&chessBoard.get(t,j)==man.NONE;t--) Bthreaten[t][j]++;
                    for(int t=j+1;t<8&&chessBoard.get(i,t)==man.NONE;t++) Bthreaten[i][t]++;
                    for(int t=j-1;t>0&&chessBoard.get(i,t)==man.NONE;t--) Bthreaten[i][t]--;
                    x=i+1;
                    y=j+1;
                    while(x<8&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x++;
                        y++;
                    }
                    x=i-1;y=j-1;
                    while(x>=0&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x--;
                        y--;
                    }
                    x=i-1;y=j+1;
                    while(x>=0&&y<8&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x--;
                        y++;
                    }
                    x=i+1;y=j-1;
                    while(x<8&&y>=0&&chessBoard.get(x,y)==man.NONE){
                        Bthreaten[x][y]++;
                        x++;
                        y--;
                    }
                    break;
                default:break;
            }
            }
    }
    public static void main(String[] args){
        // unit test
        ChessBoard chessBoard=new ChessBoard();
        MoveGenerator g=new MoveGenerator();
        g.updateThreatenMatrix(chessBoard);
        Move move=new Move();
        move.fromX=1;
        move.fromY=1;
        move.toX=1;
        move.toY=2;
        chessBoard.execute(move);

        move.fromX=2;
        move.fromY=1;
        move.toX=2;
        move.toY=2;
        chessBoard.execute(move);
        g.updateThreatenMatrix(chessBoard);
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(Integer.toString(chessBoard.get(i,j))+" ");
            }
            System.out.println("");
        }
        System.out.println("");
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(Integer.toString(g.Wthreaten[i][j])+" ");

            }
            System.out.println("");
        }
        System.out.println("");
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                System.out.print(Integer.toString(g.Bthreaten[i][j])+" ");

            }
            System.out.println("");
        }
    }
}
