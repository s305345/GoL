package sample;

/**
 * Created by Lise Estelle on 12.04.2016.
 */
public class PatternMover {
    private CellGrid beforeCells;
    private CellGrid afterCells;
    private String direction;
    int x;
    int y;
    boolean outOfBound;
    boolean safeMode;
    int moveDistance;

    public PatternMover(CellGrid beforeCells, String direction) {
        this.beforeCells = beforeCells;
        this.direction = direction;
        this.afterCells = new CellGrid(this.beforeCells.getWidth(), this.beforeCells.getHeight());
        this.x = 0;
        this.y = 0;
        this.outOfBound = false;
        this.moveDistance = 5;
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

    public CellGrid updateCells() {
        movePattern();
        if (!outOfBound || (outOfBound && !safeMode)) {
            for (int i = 0; i < beforeCells.getHeight(); i++) {
                for (int j = 0; j < beforeCells.getWidth(); j++) {
                    if (afterCells.getCell(j, i).isAlive()) {
                        beforeCells.getCell(j, i).setState(true);
                    } else {
                        beforeCells.getCell(j, i).setState(false);
                    }
                }
            }
        }
        return beforeCells;
    }

    public void movePattern() {
        setMovement();
        for (int i = 0; i < beforeCells.getHeight(); i++) {
            for (int j = 0; j < beforeCells.getWidth(); j++) {
                if (beforeCells.getCell(j, i).isAlive()) {
                    if ((j + x < 0 || j + x >= beforeCells.getWidth() || i + y < 0 || i + y >= beforeCells.getHeight())) {
                        outOfBound = true;
                    } else {
                        afterCells.getCell(j + x, i + y).setState(true);
                    }
                }

            }
        }
    }

}
