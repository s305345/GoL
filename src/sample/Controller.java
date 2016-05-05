package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {
    private static Controller theController;

    @FXML
    private Canvas theCanvas;
    @FXML
    private VBox left;
    @FXML
    private BorderPane window;
    @FXML
    private ButtonBar bot;
    @FXML
    private MenuBar top;

    @FXML
    private Slider speed;

    @FXML
    private Slider moveDistance;

    @FXML
    private Slider sizeValue;

    @FXML
    private GridPane rules;

    private int[] alive = {2, 3};
    private int[] dead = {3};

    private int moveValue = 5;
    private int cellSize = 10;
    private double scrollAmount = 1;
    private double lastPosX;
    private double lastPosY;

    private int rows;
    private int columns;

    public boolean safe;

    private Affine affine;

    public String direction;

    public static long frequencyNano = 206000000;

    private CellGrid currentGen;

    private GraphicsContext gc;

    private boolean runHeight = false;
    private boolean runWidth = false;

    private GameLoop gameLoop;

    private boolean currentMousePosState;

    private boolean isRunning = false;

    private Paint deadCellColor = Color.DARKSLATEGRAY;
    private Paint aliveCellColor = Color.AQUAMARINE;
    private Paint gridColor = Color.BLACK;


    public static Controller getController() {
        return theController;
    }

    /**
     * Calls to initialize the Controller after its root element has been completely processed.
     * Also makes it possible to use addListeners to call event handler and manipulate property.
     */
    public void initialize() {

        rows = (int)theCanvas.getHeight()/cellSize;
        columns = (int)theCanvas.getWidth()/cellSize;
        lastPosX = 0;
        lastPosY = 0;
        currentGen = new CellGrid(columns,rows);

        affine = new Affine();

        safe = true;
        theController = this;
        gameLoop = new GameLoop();
        gc = theCanvas.getGraphicsContext2D();

        /**
         * addListener method makes it possible to add event handlers and event handlers are called to change the value of a property changes.
         * A change listener, which is called whenever the value of the property has been recalculated.
         * The change listener is passed three arguments: the property whose value has changed, the previous value of the property, and the new value.
         * In our code we use addListeners for Speed(Slider), MoveDistance(Slider), CellSize(zoom of Cells in static with Slider), Resizing of Canvas and generating Cells for resizing)
         */
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


        sizeValue.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                cellSize = newValue.intValue();
                initGrid();
                rows = (int)theCanvas.getHeight()/cellSize;
                columns = (int)theCanvas.getWidth()/cellSize;
                currentGen.editSize(columns,rows);
                draw();
            }
        });

        window.widthProperty().addListener( event -> {
            if (!runWidth){
                runWidth = true;
            }else {
                theCanvas.setWidth(window.getWidth() - left.getWidth());
                columns = (int) theCanvas.getWidth() / cellSize;
                currentGen.editSize(columns, rows);
                initGrid();
                draw();
            }
        });

        window.heightProperty().addListener( event -> {
            if (!runHeight){
                runHeight = true;
            }else {
                theCanvas.setHeight(window.getHeight()-bot.getHeight()-top.getHeight());
                rows = (int)theCanvas.getHeight()/cellSize;
                currentGen.editSize(columns,rows);
                initGrid();
                draw();
            }
        });


        initGrid();
    }

    /**
     * On mouse click, sets mouse state to true or false depending what is clicked
     * @param event is triggered when the mouse is clicked
     */
    @FXML
    private void mouseClick(MouseEvent event) {
        int mousePosX = (int) (event.getX()-lastPosX) / (cellSize*(int)scrollAmount);
        int mousePosY = (int) (event.getY()-lastPosY) / (cellSize*(int)scrollAmount);
        if (!(mousePosX < 0 || mousePosX > columns - 1 || mousePosY < 0 || mousePosY > rows - 1)) {
            currentMousePosState = !currentGen.isAlive(mousePosX, mousePosY);
            if (currentMousePosState)
                currentGen.addCell(mousePosX,mousePosY);
            else
                currentGen.removeCell(mousePosX,mousePosY);

            drawCell(mousePosX, mousePosY);
        }
    }

    /**
     * Uses the state saved on mouse click to put all calls dragged over to that state
     * @param event is triggered when the mouse is dragged
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        int mousePosX = (int) (event.getX()-lastPosX) / (cellSize*(int)scrollAmount);
        int mousePosY = (int) (event.getY()-lastPosY) / (cellSize*(int)scrollAmount);
        if (!(mousePosX < 0 || mousePosX > columns - 1 || mousePosY < 0 || mousePosY > rows - 1)) {
            if (!currentGen.isAlive(mousePosX,mousePosY) && currentMousePosState)
                currentGen.addCell(mousePosX,mousePosY);
            if (currentGen.isAlive(mousePosX,mousePosY) && !currentMousePosState)
                currentGen.removeCell(mousePosX,mousePosY);
            drawCell(mousePosX, mousePosY);
        }
    }

    /**
     * this is basically just for zooming in/out using mouse scroll.
     */
    @FXML
    private void scrolling(ScrollEvent event) {
//        double posX = -((event.getX()-lastPosX)/(cellSize*scrollAmount))*cellSize;
//        double posY = -((event.getY()-lastPosY)/(cellSize*scrollAmount))*cellSize;
//        scrollAmount += event.getDeltaY()/40;
//        if (scrollAmount <= 1){
//            System.out.println("called");
//            scrollAmount = 1.0;
//            posX=0;
//            posY=0;
//        }
//        lastPosX = -posX;
//        lastPosY = -posY;
//        System.out.println(posX+" "+posY+" "+scrollAmount);
//        affine.setTx(posX);
//        affine.setTy(posY);
//        gc.setTransform(affine);
//        gc.scale(scrollAmount,scrollAmount);
//        initGrid();
//        draw();

    }

    /**
     * Activates Rules for the game.(customized by user)
     */
    @FXML
    private void activateRulesWindow() {
        rules.setVisible(true);
    }

    /**
     * changes the rules for the game according to users desire.
     * see http://en.wikipedia.org/wiki/Conway's_Game_of_Life (especially Rules)
     * for a description of how to calculate the next generation.
     */
    @FXML
    private void setRules() {
        int[] tempAlive = new int[8];
        int aliveCount = 0;
        int[] tempDead = new int[8];
        int deadCount = 0;
        int row;
        int col;
        ObservableList<Node> children = rules.getChildren();
        for (Node child : children) {
            if ((rules.getRowIndex(child) == null) || (rules.getColumnIndex(child) == null)) {
                // null is when row or col is like 0
                continue; // to skip them as there is no checkBox there
            }
            row = rules.getRowIndex(child);
            col = rules.getColumnIndex(child);
            CheckBox checkBox = (CheckBox) child;
            if (row == 1) { // alive is found on row 1
                if (checkBox.isSelected()) {
                    tempAlive[aliveCount] = col - 1;
                    aliveCount++;
                }

            } else { // dead is found on row 2
                if (checkBox.isSelected()) {
                    tempDead[deadCount] = col - 1;
                    deadCount++;
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
     * Sets all cells to false and draws a clean canvas.
     * By a clean Canvas we mean a canvas with cells and deadCell colour.
     */
    @FXML
    private void reset() {
        currentGen.reset(columns,rows);
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
     * Updates the cellGrid array and draws them on the canvas.
     * when the user clicks, this will cause the next generation to be
     * created and displayed.
     */
    @FXML
    private void nextFrame() {
        update();
        draw();
    }

    /**
     * Gives the ability to the user to choose a desired predefined Pattern for the game.
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
                RLE rle = new RLE(s, currentGen, columns, rows);
                try {
                    rle.codeToCells();
                    draw();
                }catch (StackOverflowError error){
                    System.out.println("Code was too big");
                }
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
     * Gives some movement options(left, up, right and down) to the game and it is adjustable from 1 to 10.
     * Up, down, left and right are triggered when the user clicks on the related buttons.
     * Moves the canvas content(columns and rows of Cells) to the desired direction.
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
     * Enables movement for moveDistance in Canvas.
     */
    public void move() {
        PatternMover mover = new PatternMover(currentGen, direction, rows, columns);
        mover.setMode(safe);
        mover.setMoveDistance(moveValue);
        mover.updateCells();
        draw();

    }


    /**
     * Iterates the next generation.
     * Updating and bringing up the alive cells and dead cells according to the game rules.
     */
    public void update() {
        CellGrid nextGen = new CellGrid(columns,rows);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                currentGen.nextGen(x,y,nextGen,alive,dead);
            }
        }
        currentGen = nextGen;
    }

    /**
     * Draws cells from the desired CellGrid.
     * Cells are defined in columns and rows.
     */
    public void draw() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                drawCell(x, y);
            }
        }
    }

    /**
     * Draws one cell from it's position and state
     */
    private void drawCell(int x, int y) {
        if (currentGen.isAlive(x,y)) {
            gc.setFill(aliveCellColor);
        } else {
            gc.setFill(deadCellColor);
        }
        gc.fillRect(x * cellSize + 1, y * cellSize + 1, cellSize-2, cellSize-2);
        //System.out.println(currentGen.toString());
    }

    /**
     * Draws a clean canvas without taking in account cell states(dead or alive).
     * If you use this you should make sure all cells in CellGrid are set to "false".
     */
    private void initGrid() { // Draws a grid line for line
        gc.setFill(deadCellColor);
        gc.fillRect(0, 0, theCanvas.getWidth(), theCanvas.getHeight());
        gc.setStroke(gridColor);
        gc.setLineWidth(2.0);
        for (int x = 0; x < theCanvas.getWidth(); x += cellSize) {
            gc.strokeLine(x, 0.0, x, theCanvas.getHeight());
            gc.stroke();

        }
        for (int y = 0; y < theCanvas.getHeight(); y += cellSize) {
            gc.strokeLine(0.0, y, theCanvas.getWidth(), y);
            gc.stroke();
        }

    }

}
