package sample;

/**
 * The CellGrid class is where cell information is stored (in 2D arrays).
 */
public class CellGrid {
    private Cell cells[][];

    /**
     * CellGrid constructor that initializes a 2D array with new cells of the desired height and width
     *
     * @param w is the width of the 2D array
     * @param h is the height of the 2D array
     */
    public CellGrid(int w, int h) {
        cells = new Cell[h][w];
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                replace(x, y, new Cell(false));
            }
        }
    }

    /**
     * Replaces a cell with another cell
     * @param x is the desired horizontal placement of a cell in a CellGrid
     * @param y is the desired vertical placement of a cell in a CellGrid
     * @param cell is the cell you wish to store in the CellGrid
     */
    public void replace(int x, int y, Cell cell) {
        cells[y][x] = cell;
    }

    /**
     * Method to get board width
     * @return board width
     */
    public int getWidth() {
        return cells[0].length;
    }

    /**
     * Method to get board height
     * @return board height
     */
    public int getHeight() {
        return cells.length;
    }

    /**
     * Method to get one specific cell out of the cellGrid
     * @return the desired cell
     */
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return null;
        }
        return cells[y][x];
    }

    /**
     * Sets all cells to false
     */
    public void reset() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                getCell(x, y).setState(false);
            }
        }
    }
}
