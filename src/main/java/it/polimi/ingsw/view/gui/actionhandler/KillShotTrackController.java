package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class KillShotTrackController implements Ifxml<StackPane>
{
    @FXML private ImageView trackImageView;
    @FXML private StackPane rootPane;

    public void initialize() {
        trackImageView.setImage(new Image("killShotTrack.png"));
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static KillShotTrackController getController(){
        return GUIView.getController("/view/ActionHandler/killShotTrack/killShotTrack.fxml", "/view/ActionHandler/killShotTrack/killShotTrack.css");
    }
}
