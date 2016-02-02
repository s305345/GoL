package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    @FXML private Canvas theCanvas;
    public void initialize(){
        GraphicsContext gc = theCanvas.getGraphicsContext2D();
        draw(gc);


        boolean cells[][] = new boolean[200][300];

    }

    private void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(1, 1, 20, 20);

    }

}
