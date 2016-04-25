package sample;

import java.util.stream.IntStream;

/**
 * The cell class stores the cell's state (true or false)
 */
public class Cell {
    private boolean isAlive = false;

    /**
     * Cell constructor
     *
     * @param isAlive initialises the cell with true or false
     */
    Cell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    /**
     * checks if the cell is alive or dead
     * @return cell's state
     */
    public boolean isAlive() {
        return this.isAlive;
    }

    /**
     * Method to change an existing's cell's state
     * @param state true(alive) or false(dead)
     */
    public void setState(boolean state) {
        this.isAlive = state;
    }

    /**
     * Also called next generation, this method updated the gameboard with the new generation of cells through rules.
     * It checks only one cell.
     *
     * @param x,y        the desired cell to update
     * @param oldCells   the controller's CellGrid
     * @param newCells   the new generation
     * @param alive,dead are lists of how many neighbors are needed for a cell to live in the next generation
     */
    public void updateByRules(int x, int y, CellGrid oldCells, CellGrid newCells, int[] alive, int[] dead) {
        if (oldCells.getCell(x, y).isAlive() && IntStream.of(alive).anyMatch(i -> i == getLiveNeighbors(x, y, oldCells))){
            newCells.getCell(x, y).setState(true);
        } else if (!oldCells.getCell(x, y).isAlive() && IntStream.of(dead).anyMatch(i -> i == getLiveNeighbors(x, y, oldCells))){
            newCells.getCell(x, y).setState(true);
        } else {
            newCells.getCell(x, y).setState(false);
        }
    }

    /**
     * Counts how many of a cell's neighbors are alive
     * @return number of living neighbors
     */
    private int getLiveNeighbors(int x, int y, CellGrid cells) {
        int count = 0;

        count += isAlive(cells.getCell(x - 1, y - 1));
        count += isAlive(cells.getCell(x, y - 1));
        count += isAlive(cells.getCell(x + 1, y - 1));

        count += isAlive(cells.getCell(x - 1, y));
        count += isAlive(cells.getCell(x + 1, y));

        count += isAlive(cells.getCell(x - 1, y + 1));
        count += isAlive(cells.getCell(x, y + 1));
        count += isAlive(cells.getCell(x + 1, y + 1));

        return count;
    }

    /**
     * Checks if a cell is alive or dead
     * @return true or false
     */
    private int isAlive(Cell cell) {
        if (cell == null)
            return 0;

        return cell.isAlive() ? 1 : 0;

    }

}
