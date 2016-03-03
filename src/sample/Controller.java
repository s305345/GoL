package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Controller {
    private static Controller theController;

    @FXML
    private Canvas theCanvas;

    @FXML
    private Slider speed;

    public static long frequencyNano;

    private CellGrid currentGen;

    private GraphicsContext gc;

    private GameLoop gameLoop;

    private boolean isRunning = false;

    private Paint deadCellColor = Color.DARKSLATEGRAY;
    private Paint aliveCellColor = Color.AQUAMARINE;
    private Paint gridColor = Color.BLACK;


    public static Controller getController() {
        return theController;
    }

    public void initialize() {
        theController = this;
        gameLoop = new GameLoop();
        gc = theCanvas.getGraphicsContext2D();
        initGrid();

        theCanvas.onMouseClickedProperty();
        theCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int mousePosX = (int) event.getX() / 10;
                int mousePosY = (int) event.getY() / 10;
                currentGen.getCell(mousePosX, mousePosY).switchState();
                drawCell(mousePosX, mousePosY);
            }
        });

        speed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                frequencyNano = newValue.intValue();
            }
        });

        currentGen = new CellGrid((int) theCanvas.getWidth() / 10, (int) theCanvas.getHeight() / 10);

    }


    @FXML
    private void reset() {
        currentGen.reset();
        initGrid();
    }

    @FXML
    private void play() {
        if (!isRunning) {
            gameLoop.start();
        } else {
            gameLoop.stop();
        }
        isRunning = !isRunning;
    }

    @FXML
    private void nextFrame() {
        update();
        draw();
    }

    public void update() {
        CellGrid nextGen = new CellGrid(currentGen.getWidth(), currentGen.getHeight());
        for (int y = 0; y < currentGen.getHeight(); y++) {
            for (int x = 0; x < currentGen.getWidth(); x++) {
                Cell cell = currentGen.getCell(x, y);
                cell.update(x, y, currentGen, nextGen);
            }
        }
        currentGen = nextGen;
    }

    public void draw() {
        for (int y = 0; y < currentGen.getHeight(); y++) {
            for (int x = 0; x < currentGen.getWidth(); x++) {
                drawCell(x, y);
            }
        }
    }

    private void drawCell(int x, int y) {
        Cell cell = currentGen.getCell(x, y);
        if (cell.isAlive()) {
            gc.setFill(aliveCellColor);
        } else {
            gc.setFill(deadCellColor);
        }
        gc.fillRect(x * 10 + 1, y * 10 + 1, 8, 8);
    }

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
