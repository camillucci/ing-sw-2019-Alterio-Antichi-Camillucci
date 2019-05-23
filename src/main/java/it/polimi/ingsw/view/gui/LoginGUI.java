package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.Login;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LoginGUI extends Login
{
    public TextField loginText;
    @FXML private Label robotText;

    @FXML
    protected void initialize(){
        Executors.newSingleThreadScheduledExecutor().schedule(this::animation, 1, TimeUnit.SECONDS);
    }
    private void robotSpeach(String str, Runnable onEnd){
        robotSpeach(str,60, onEnd);
    }

    private void robotSpeach(String str, int duration, Runnable onEnd){
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration),
                event -> {
                    if (i.get() > str.length()) {
                        timeline.stop();
                        onEnd.run();
                    } else {
                        robotText.setText(str.substring(0, i.get()));
                        i.set(i.get() + 1);
                    }
                });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void animation(){
        String destroyerStr = "Hey, my name is :D-STRUCT-0R,";
        String welcomeStr = "Welcome to Adrenaline!";
        String usernameStr = "Please, choose a nickname!";
        String str3 = "i can choose for you";
        String str4 = "turanga_lella is perfect!";
        String str2 = "mmh, maybe..";
        robotSpeach(destroyerStr, () -> robotSpeach(welcomeStr, () -> robotSpeach(usernameStr, () -> robotSpeach(str2, 140, () -> robotSpeach(str3, () -> robotSpeach(str4, () -> {
            loginText.setStyle("-fx-border-color: #1abc9c;");
            loginText.setText("turanga_lella");
        }))))));


    }

    @Override
    public void notifyAccepted(boolean accepted) {
        //todo
    }

    @Override
    public void notifyAvailableColor(List<String> availableColors) {
        //todo
    }

    @Override
    public void notifyHost(boolean isHost) throws IOException {
        //todo
    }

    @Override
    public void login() {
    }

    @Override
    public void onNewMessage(String message) {
        //todo
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }

}
