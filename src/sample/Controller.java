package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    @FXML private Canvas theCanvas;
    public void initialize(){
        System.out.println("Hallo!");
        GraphicsContext gc = theCanvas.getGraphicsContext2D();
        draw(gc);

    }

    private void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(1, 1, 100, 100);

    }

}
