package sample;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    private long prevTime = 0;

    @Override
    public void handle(long now) {
        if (prevTime == 0) {
            prevTime = now;
        }
        if (now - prevTime > Controller.frequencyNano) {
            Controller.getController().update();
            Controller.getController().draw();
            prevTime = now;
        }
    }
}
