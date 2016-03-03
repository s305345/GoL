package sample;

public class Cell {
    private boolean isAlive = false;

    Cell(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void switchState() {
        this.isAlive = !this.isAlive;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setState(boolean state) {
        this.isAlive = state;
    }

    public void update(int x, int y, CellGrid oldCells, CellGrid newCells) {
        if (oldCells.getCell(x, y).isAlive() && (getLiveNeighbors(x, y, oldCells) == 3 || getLiveNeighbors(x, y, oldCells) == 2)) {
            newCells.getCell(x, y).setState(true);
        } else if (oldCells.getCell(x, y).isAlive() && (getLiveNeighbors(x, y, oldCells) < 2 || getLiveNeighbors(x, y, oldCells) > 3)) {
            newCells.getCell(x, y).setState(false);
        } else if (!oldCells.getCell(x, y).isAlive() && getLiveNeighbors(x, y, oldCells) == 3) {
            newCells.getCell(x, y).setState(true);
        } else {
            if (oldCells.getCell(x, y).isAlive()) {
                newCells.getCell(x, y).setState(true);
            } else {
                newCells.getCell(x, y).setState(false);
            }
        }
    }

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

    private int isAlive(Cell cell) {
        if (cell == null)
            return 0;

        return cell.isAlive() ? 1 : 0;

    }

}
