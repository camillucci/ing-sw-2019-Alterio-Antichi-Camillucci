package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class IpAddressController implements Ifxml<HBox>
{
    @FXML
    private HBox hBox;
    @FXML private TextField ipAddressText;
    @FXML private Button nextButton;

    public static IpAddressController getController() {
        return GUIView.getController("/view/login/ipAddressHBox/IpAddressHBox.fxml", "/view/login/ipAddressHBox/IpAddressHBox.css");
    }

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public TextField getIpAddressText() {
        return ipAddressText;
    }

    public Button getNextButton() {
        return nextButton;
    }
}
