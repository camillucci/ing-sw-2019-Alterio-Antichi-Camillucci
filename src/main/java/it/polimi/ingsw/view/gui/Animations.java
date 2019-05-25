package it.polimi.ingsw.view.gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Animations
{
    public static Timeline autoWriteLabel(Label label, String text, int millisecondsPerCar){
        return autoWriteLabel(label, text, millisecondsPerCar, () -> {});
    }
    public static Timeline autoWriteLabel(Label label, String text, int millisecondsPerCar, Runnable onEnd){
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(millisecondsPerCar),
                e -> {
                    if (i.get() > text.length()) {
                        timeline.stop();
                        onEnd.run();
                    } else {
                        label.setText(text.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return timeline;
    }
}
