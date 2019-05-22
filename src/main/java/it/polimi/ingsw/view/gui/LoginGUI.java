package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.Login;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class LoginGUI extends Login {
    public Text randomText;

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
        //todo
    }

    @Override
    public void onNewMessage(String message) {
        //todo
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }

    public void onLoginClicked_EventHandler(ActionEvent actionEvent)
    {
        randomText.setText("oh boy");
    }
}
