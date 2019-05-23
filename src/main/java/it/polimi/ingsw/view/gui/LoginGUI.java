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

public class LoginGUI extends Login
{
    public TextField loginText;
    @FXML private Label robotText;

    @FXML
    protected void initialize(){
        animation();
    }

    private void robotSpeach(String str, Runnable onEnd){
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(60),
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
        robotSpeach(destroyerStr, () -> robotSpeach(welcomeStr, () -> robotSpeach(usernameStr, () -> {
            loginText.setStyle("-fx-border-color: #1abc9c;");
        })));
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
