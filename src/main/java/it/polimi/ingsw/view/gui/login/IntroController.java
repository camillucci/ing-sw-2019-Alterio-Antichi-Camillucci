package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class IntroController implements Ifxml<HBox> {
    @FXML private HBox hBox;
    @FXML private Button rmiButton;
    @FXML private Button socketButton;

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public static IntroController getController(){
        return GUIView.getController("/view/login/IntroHBox/Intro.fxml", "/view/login/IntroHBox/Intro.css");
    }

    public Button getRMIButton() {
        return rmiButton;
    }

    public Button getSocketButton() {
        return socketButton;
    }
}
