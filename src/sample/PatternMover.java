package sample;

/**
 * Created by Lise Estelle on 12.04.2016.
 */
public class PatternMover {
    private CellGrid cells;
    private CellGrid beforeCells;
    int rows;
    int columns;
    private String direction;
    int x;
    int y;
    boolean outOfBound;
    boolean safeMode;
    int moveDistance;

    public PatternMover(CellGrid cells, String direction, int rows, int columns) {
        this.cells = new CellGrid(columns,rows);
        this.direction = direction;
        this.x = 0;
        this.y = 0;
        this.outOfBound = false;
        this.moveDistance = 5;
        this.rows = rows;
        this.columns = columns;
        this.beforeCells = cells;
    }

    public void setMode(boolean desiredMode) {
        this.safeMode = desiredMode;
    }

    public void setMoveDistance(int desiredDistance) {
        this.moveDistance = desiredDistance;
    }

    public void setMovement() {
        if (direction == "up") {
            y = -moveDistance;

        } else if (direction == "down") {
            y = moveDistance;

        } else if (direction == "left") {
            x = -moveDistance;

        } else if (direction == "right") {
            x = moveDistance;

        } else {
            System.out.println("Didn't get the right direction input");
        }

    }

    public void updateCells() {
        movePattern();
        if (!outOfBound || (outOfBound && !safeMode)) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (cells.isAlive(j,i)) {
                        beforeCells.setState(true,j,i);
                    } else {
                        beforeCells.setState(false,j,i);
                    }
                }
            }
        }
    }

    public void movePattern() {
        setMovement();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (beforeCells.isAlive(j,i)) {
                    //System.out.println("hi hi");
                    if ((j + x < 0 || j + x >= columns || i + y < 0 || i + y >= rows)) {
                        outOfBound = true;
                    } else {
                        cells.addCell(j+x,i+y);
                    }
                }
            }
        }
    }

}
