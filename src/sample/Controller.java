package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    private Canvas theCanvas;

    public void initialize() {
        System.out.println("Hallo!");
        GraphicsContext gc = theCanvas.getGraphicsContext2D();
        draw(gc);

    }

    private void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int x = 0; x < theCanvas.getWidth(); x += 10) {
            double x1 = x;

        gc.moveTo(x1, 0);
        gc.lineTo(x1, theCanvas.getHeight());
        gc.stroke();

    }

    for(int y = 0;y<theCanvas.getHeight();y+=10)

    {
        double y1 = y;

        gc.moveTo(0, y1);
        gc.lineTo(theCanvas.getWidth(), y1);
        gc.stroke();
    }
}

}
