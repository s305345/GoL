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

    /**
     * Constructs and initialises all variables.
     *
     * @param cells     constructs a new cell object
     * @param direction saves direction, then later it's used to either move a pattern in the direction the user wants.
     * @param rows      adding rows
     * @param columns   adding colums.
     */
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

    /**
     * Information is collected, the information is gathered from the user
     * input and its gets saved, and therefor can be used in other methods later.
     * @param desiredMode is the required input
     */
    public void setMode(boolean desiredMode) {
        this.safeMode = desiredMode;
    }

    /**
     * Gathers information from user input, saved and therefore can be used in other methods later.
     * @param desiredDistance is the required input.
     */
    public void setMoveDistance(int desiredDistance) {
        this.moveDistance = desiredDistance;
    }

    /**
     * Gathers information from user input, saved and therefor can be used in other methods
     * saves information about movement of uploaded pattern.
     */
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

    /**
     * Iterates the next generation.
     * checks previous methods and uses this to create a updated
     */
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

    /**
     * moves pattern to desired location after collecting
     * information from user input.
     */
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
