package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Controller {
    private static Controller theController;

    @FXML
    private Canvas theCanvas;

    @FXML
    private Slider speed;

    @FXML
    private Slider moveDistance;

    @FXML
    private GridPane rules;

    private int[] alive = {2, 3};

    private int[] dead = {3};

    private int moveValue = 5;

    public boolean safe;

    public String direction;

    public static long frequencyNano = 206000000;

    private CellGrid currentGen;

    private GraphicsContext gc;

    private GameLoop gameLoop;

    private boolean currentMousePosState;

    private boolean isRunning = false;

    private Paint deadCellColor = Color.DARKSLATEGRAY;
    private Paint aliveCellColor = Color.AQUAMARINE;
    private Paint gridColor = Color.BLACK;


    public static Controller getController() {
        return theController;
    }

    public void initialize() {
        safe = true;
        theController = this;
        gameLoop = new GameLoop();
        gc = theCanvas.getGraphicsContext2D();
        initGrid();

        speed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                frequencyNano = newValue.intValue();
            }
        });

        moveDistance.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                moveValue = newValue.intValue();
            }
        });


        currentGen = new CellGrid((int) theCanvas.getWidth() / 10, (int) theCanvas.getHeight() / 10);

    }

    /**
     * On mouse click, sets mouse state to true or false depending what is clicked
     *
     * @param event is triggered when the mouse is clicked
     */
    @FXML
    private void mouseClick(MouseEvent event) {
        int mousePosX = (int) event.getX() / 10;
        int mousePosY = (int) event.getY() / 10;
        if (!(mousePosX < 0 || mousePosX > theCanvas.getWidth() / 10 - 1 || mousePosY < 0 || mousePosY > theCanvas.getHeight() / 10 - 1)) {
            currentMousePosState = !currentGen.getCell(mousePosX, mousePosY).isAlive();
            currentGen.getCell(mousePosX, mousePosY).setState(currentMousePosState);
            drawCell(mousePosX, mousePosY);
        }
    }

    /**
     * Uses the state saved on mouse click to put all calls dragged over to that state
     * @param event is triggered when the mouse is dragged
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        int mousePosX = (int) event.getX() / 10;
        int mousePosY = (int) event.getY() / 10;
        if (!(mousePosX < 0 || mousePosX > theCanvas.getWidth() / 10 - 1 || mousePosY < 0 || mousePosY > theCanvas.getHeight() / 10 - 1)) {
            currentGen.getCell(mousePosX, mousePosY).setState(currentMousePosState);
            drawCell(mousePosX, mousePosY);
        }
    }

    @FXML
    private void activateRulesWindow() {
        rules.setVisible(true);
    }

    @FXML
    private void setRules() {
        int[] tempAlive = new int[8];
        int aliveCount = 0;
        int[] tempDead = new int[8];
        int deadCount = 0;
        for (int row = 1; row <= 2; row++) {
            for (int col = 1; col <= 9; col++) {
                ObservableList<Node> children = rules.getChildren();
                for (Node child : children) {
                    if ((rules.getRowIndex(child) == null) || (rules.getColumnIndex(child) == null)) {
                    } else if ((rules.getRowIndex(child) == row) && (rules.getColumnIndex(child) == col)) {
                        CheckBox checkBox = (CheckBox) child;
                        if (row == 1) {
                            if (checkBox.isSelected()) {
                                tempAlive[aliveCount] = Math.max(0, col - 1);
                                aliveCount++;
                            }

                        } else {
                            if (checkBox.isSelected()) {
                                tempDead[deadCount] = Math.max(0, col - 1);
                                deadCount++;
                            }
                        }
                    }
                }
            }
        }
        alive = new int[aliveCount];
        dead = new int[deadCount];
        arrayToArray(alive, tempAlive);
        arrayToArray(dead, tempDead);

        rules.setVisible(false);
    }

    public int[] arrayToArray(int[] finalArray, int[] containsValues) {
        for (int i = 0; i < finalArray.length; i++) {
            finalArray[i] = containsValues[i];
        }
        return finalArray;
    }

    public void arrayToString(int[] theArray) {
        for (int i = 0; i < theArray.length; i++) {
            System.out.println(theArray[i]);
        }
    }

    /**
     * Set's all cells to false and draws a clean canvas
     */
    @FXML
    private void reset() {
        currentGen.reset();
        initGrid();
    }

    /**
     * starts a timer that updates the grid at a desired speed
     */
    @FXML
    private void play() {
        if (!isRunning) {
            gameLoop.start();
        } else {
            gameLoop.stop();
        }
        isRunning = !isRunning;
    }

    /**
     * Updates the cellgrid array and draws them on the canvas
     */
    @FXML
    private void nextFrame() {
        update();
        draw();
    }

    /**
     * Opens a file chooser and when a file has been chosen, reads the file, updates the cells, draws them on the canvas.
     */
    @FXML
    private void patternChoser() {
        // User chooses path
        FileChooser en = new FileChooser();
        en.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("rle Files", "*.rle"));
        File file1 = en.showOpenDialog(null);

        // Code uses chosen path
        if (file1 != null) {
            File file = new File(file1.getAbsolutePath());

            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                byte fileContent[] = new byte[(int) file.length()];
                fin.read(fileContent);
                String s = new String(fileContent);
                RLE rle = new RLE(s, currentGen);
                rle.codeToCells();
                draw();
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException ioe) {
                System.out.println("Exception while reading file " + ioe);
            } finally {
                // close the streams using close method
                try {
                    if (fin != null) {
                        fin.close();
                    }
                } catch (IOException ioe) {
                    System.out.println("Error while closing stream: " + ioe);
                }
            }
        }
    }

    /**
     * Up, down, left and right are triggered when the user clicks on the related buttons.
     * Moves the canvas content to the desired direction
     */
    @FXML
    public void up() {
        direction = "up";
        move();
    }

    @FXML
    public void down() {
        direction = "down";
        move();
    }

    @FXML
    public void left() {
        direction = "left";
        move();
    }

    @FXML
    public void right() {
        direction = "right";
        move();
    }

    @FXML
    public void safe() {
        safe = !safe;
    }


    /**
     * Reads the cells and moves them to a desired direction and desired distance.
     */
    public void move() {
        PatternMover mover = new PatternMover(currentGen, direction);
        mover.setMode(safe);
        mover.setMoveDistance(moveValue);
        mover.updateCells();
        draw();

    }


    /**
     * Iterates the next generation
     */
    public void update() {
        CellGrid nextGen = new CellGrid(currentGen.getWidth(), currentGen.getHeight());
        for (int y = 0; y < currentGen.getHeight(); y++) {
            for (int x = 0; x < currentGen.getWidth(); x++) {
                Cell cell = currentGen.getCell(x, y);
                cell.updateByRules(x, y, currentGen, nextGen, alive,dead);
            }
        }
        currentGen = nextGen;
    }

    /**
     * Draws cells from the desired CellGrid
     */
    public void draw() {
        for (int y = 0; y < currentGen.getHeight(); y++) {
            for (int x = 0; x < currentGen.getWidth(); x++) {
                drawCell(x, y);
            }
        }
    }

    /**
     * Draws one cell from it's position and state
     */
    private void drawCell(int x, int y) {
        Cell cell = currentGen.getCell(x, y);
        if (cell.isAlive()) {
            gc.setFill(aliveCellColor);
        } else {
            gc.setFill(deadCellColor);
        }
        gc.fillRect(x * 10 + 1, y * 10 + 1, 8, 8);
    }

    /**
     * Draws a clean canvas without taking in account cell states
     * If you use this you should make sure all cells in CellGrid are set to "false"
     */
    private void initGrid() { // Draws a grid line for line
        gc.setFill(deadCellColor);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
        gc.setStroke(gridColor);
        gc.setLineWidth(2.0);
        for (int x = 0; x < theCanvas.getWidth(); x += 10) {
            gc.strokeLine(x, 0.0, x, theCanvas.getHeight());
            gc.stroke();

        }
        for (int y = 0; y < theCanvas.getHeight(); y += 10) {
            gc.strokeLine(0.0, y, theCanvas.getWidth(), y);
            gc.stroke();
        }

    }

}
