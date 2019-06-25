package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;

public class SquareController implements Ifxml<StackPane> {
    @FXML private Rectangle rec;
    @FXML private ImageView ammoCard_shop;
    @FXML private StackPane squareRootPane;
    @FXML private Avatar avatar1;
    @FXML private Avatar avatar2;
    @FXML private Avatar avatar3;
    @FXML private Avatar avatar4;
    @FXML private Avatar avatar5;
    private List<Avatar> avatars;
    private int totPlayers = 0;

    private void putPlayer(String color)
    {
        avatars.get(totPlayers++).setColor(color);
    }

    public void initialize(){
        avatars = getAvatars();
    }

    private void clear(){
        totPlayers = 0;
        for(ImageView avatar : avatars)
            avatar.setImage(null);
    }

    public void onModelChanged(SquareSnapshot square){
        clear();
        if(square != null)
            for(String color : square.getColors())
                putPlayer(color);
    }

    public void setClickable(EventHandler<MouseEvent> eventHandler){
        squareRootPane.setOnMouseEntered(e -> rec.setVisible(true));
        squareRootPane.setOnMouseExited(e -> rec.setVisible(false));
        this.squareRootPane.setOnMouseClicked(eventHandler);
    }

    public void setClickable(Avatar avatar, EventHandler<MouseEvent> eventHandler){
        avatar.getStyleClass().add("button");
        avatar.setOnMouseClicked(eventHandler);
    }

    public void reset(){
        this.rec.setVisible(false);
        squareRootPane.setOnMouseEntered(null);
        squareRootPane.setOnMouseExited(null);
        this.squareRootPane.setOnMouseClicked(null);
        for(Avatar avatar : avatars)
        {
            avatar.getStyleClass().remove("button");
            avatar.setOnMouseClicked(null);
        }
    }

    public List<Avatar> getAvatars(){
        return Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5);
    }

    @Override
    public StackPane getRoot() {
        return squareRootPane;
    }

    public static SquareController getController()
    {
        return GUIView.getController("/view/ActionHandler/map/square/square.fxml", "/view/ActionHandler/map/square/square.css");
    }

}
