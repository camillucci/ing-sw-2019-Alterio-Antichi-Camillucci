package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class NicknameController implements Ifxml<HBox>
{
    @FXML private HBox hBox;
    @FXML private TextField loginText;
    @FXML private Button nextButton;

    public static NicknameController getController() {
        return GUIView.getController("/view/login/nicknameHBox/NicknameHBox.fxml", "/view/login/nicknameHBox/NicknameHBox.css");
    }

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public TextField getLoginText() {
        return loginText;
    }

    public Button getNextButton() {
        return nextButton;
    }
}
