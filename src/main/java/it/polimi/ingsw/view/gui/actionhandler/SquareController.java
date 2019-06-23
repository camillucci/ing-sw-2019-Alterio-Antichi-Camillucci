package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class SquareController implements Ifxml<StackPane> {
    @FXML private ImageView ammoCard;
    @FXML private StackPane squareRootPane;
    @FXML private ImageView avatar1;
    @FXML private ImageView avatar2;
    @FXML private ImageView avatar3;
    @FXML private ImageView avatar4;
    @FXML private ImageView avatar5;

    public void initialize() {
        /*
        for (ImageView avatar : getAvatars())
            avatar.setImage(new Image("player/violet_avatar.png"));
        ammoCard.setImage(new Image("ammo/1r_1y_pu.png"));

         */
    }

    public List<ImageView> getAvatars(){
        return Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5);
    }

    @Override
    public StackPane getRoot() {
        return squareRootPane;
    }

    public static SquareController getController(){
        return GUIView.getController("/view/ActionHandler/map/square/square.fxml", "/view/ActionHandler/map/square/square.css");
    }

}
