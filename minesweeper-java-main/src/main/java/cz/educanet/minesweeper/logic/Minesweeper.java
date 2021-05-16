package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {
    private int rows;
    private int columns;
    private Fields[][] field;
    private int bombs;
    private boolean clicked;
    private int flags;
    private int sq;

    public Minesweeper(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        field = new Fields[columns][rows];
        bombs = 25;
        clicked = false;
        flags = bombs;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                field[i][j] = new Fields();
            }
        }
        BombsIn(rows, columns);
        sq = rows * columns - bombs;
    }



    public int getField(int x, int y) {
        return field[x][y].getState();
    }


    public void toggleFieldState(int x, int y) {
        if (field[x][y].getState() == 0) field[x][y].setState(2);
        else if (field[x][y].getState() == 2){ field[x][y].setState(3); }
        else if (field[x][y].getState() == 3){ field[x][y].setState(0); }
        flags--;
    }


    public void reveal(int x, int y) {
        if (field[x][y].getBomb()) { clicked = true; }
        recurseReveal(x, y);
        sq--;
    }

    private void recurseReveal(int x, int y) {
        field[x][y].setState(1);
        if (getAdjacentBombCount(x, y) == 0) {
            boolean LH = (x != 0) && (y != 0);
            boolean VH = (x != columns - 1) && (y != 0);
            boolean DL = (x != 0) && (y != rows - 1);
            boolean DP = (x != columns - 1) && (y != rows - 1);

            if (LH && !field[x - 1][y - 1].getBomb() && field[x - 1][y - 1].getState() != 1) { recurseReveal(x - 1, y - 1); sq--; }
            if ((LH || VH) && !field[x][y - 1].getBomb() && field[x][y - 1].getState() != 1) { sq--; recurseReveal(x, y - 1); }
            if (VH && !field[x + 1][y - 1].getBomb() && field[x + 1][y - 1].getState() != 1) { sq--; recurseReveal(x + 1, y - 1); }
            if ((VH || DP) && !field[x + 1][y].getBomb() && field[x + 1][y].getState() != 1) { sq--; recurseReveal(x + 1, y); }
            if (DP && !field[x + 1][y + 1].getBomb() && field[x + 1][y + 1].getState() != 1) { sq--; recurseReveal(x + 1, y + 1); }
            if ((DP || DL) && !field[x][y + 1].getBomb() && field[x][y + 1].getState() != 1) { sq--; recurseReveal(x, y + 1); }
            if (DL && !field[x - 1][y + 1].getBomb() && field[x - 1][y + 1].getState() != 1) { sq--; recurseReveal(x - 1, y + 1); }
            if ((DL || LH) && !field[x - 1][y].getBomb() && field[x - 1][y].getState() != 1) { sq--; recurseReveal(x - 1, y); }
        }
    }
    public int getAdjacentBombCount(int x, int y) {
        int count = 0;
        boolean LH = (x != 0) && (y != 0);
        boolean VP = (x != columns - 1) && (y != 0);
        boolean DL = (x != 0) && (y != rows - 1);
        boolean DP = (x != columns - 1) && (y != rows - 1);

        if (LH && field[x - 1][y - 1].getBomb()) count++;
        if ((LH || VP) && field[x][y - 1].getBomb()) count++;
        if (VP && field[x + 1][y - 1].getBomb()) count++;
        if ((VP || DP) && field[x + 1][y].getBomb()) count++;
        if (DP && field[x + 1][y + 1].getBomb()) count++;
        if ((DP || DL) && field[x][y + 1].getBomb()) count++;
        if (DL && field[x - 1][y + 1].getBomb()) count++;
        if ((DL || LH) && field[x - 1][y].getBomb()) count++;

        return count;
    }

    public boolean isBombOnPosition(int x, int y) { return (field[x][y].getBomb()); }

    public Fields[][] BombsIn(int rows, int columns) {
        Random random = new Random();
        int counter = 0;
        while (counter != bombs) {
            int x = random.nextInt(columns);
            int y = random.nextInt(rows);
            while (field[x][y].getBomb()) {
                x = random.nextInt(columns);
                y = random.nextInt(rows);
            }
            field[x][y].setBomb(true);
            counter++;
        }
        return field;
    }


    public int getBombCount() {
        return bombs;
    }


    public int getRemainingBombCount() {
        return bombs - flags;
    }


    public boolean didWin() {
        return (sq == 0);
    }


    public boolean didLoose() {
        if (clicked) {
            return true;
        }
        return false;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

}
