package sample;


public class CellGrid {
    private Cell cells[][];

    public CellGrid(int w, int h) {
        cells = new Cell[h][w];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                replace(x, y, new Cell(false));
            }
        }
    }

    public void replace(int x, int y, Cell cell) {
        cells[y][x] = cell;
    }

    public int getWidth() {
        return cells[0].length;
    }

    public int getHeight() {
        return cells.length;
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return cells[y][x];
    }

    public void reset() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                getCell(x, y).setState(false);
            }
        }
    }


}
