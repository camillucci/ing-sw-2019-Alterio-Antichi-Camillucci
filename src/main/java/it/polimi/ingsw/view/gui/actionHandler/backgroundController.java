package it.polimi.ingsw.view.gui.actionHandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class backgroundController implements Ifxml<AnchorPane> {
    @FXML private AnchorPane anchorPane;

    @Override
    public AnchorPane getRoot() {
        return anchorPane;
    }

    public static backgroundController getController(){
        return GUIView.getController("/view/ActionHandler/General/background.fxml", "/view/login/IntroHBox/background.css");
    }
}
