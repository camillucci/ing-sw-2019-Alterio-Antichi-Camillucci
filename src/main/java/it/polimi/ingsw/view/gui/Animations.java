package it.polimi.ingsw.view.gui;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Animations
{
    private Animations() {}

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

    public static void zoomAnimation(Node node, double xScale, double yScale)
    {
        zoomAnimation(node, xScale, yScale, true);
    }

    public static void zoomAnimation(Node node, double xScale, double yScale, boolean reverse)
    {
        ScaleTransition zoomTransition = new ScaleTransition();
        zoomTransition.setDuration(Duration.millis(300));
        zoomTransition.setNode(node);
        zoomTransition.setByY(yScale);
        zoomTransition.setByX(xScale);
        zoomTransition.setCycleCount(2);
        zoomTransition.setAutoReverse(reverse);
        zoomTransition.play();
    }

    public static RotateTransition rotatingAnimation(Node node)
    {
        RotateTransition rt = new RotateTransition(Duration.millis(1500), node);
        rt.setByAngle(600);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setAutoReverse(true);
        return rt;
    }

    public static void disappearAnimation(Node node, Runnable onFinish)
    {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0);
        st.setToY(0);
        st.play();

        st.play();
        st.setOnFinished(e -> onFinish.run());

    }

    public static void appearAnimation(Node node)
    {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();

        st.play();
    }
}
