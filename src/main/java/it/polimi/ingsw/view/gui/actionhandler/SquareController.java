package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

public class SquareController implements Ifxml<StackPane> {
    @FXML private ImageView ammoCard_shop;
    @FXML private StackPane squareRootPane;
    @FXML private ImageView avatar1;
    @FXML private ImageView avatar2;
    @FXML private ImageView avatar3;
    @FXML private ImageView avatar4;
    @FXML private ImageView avatar5;
    private List<ImageView> avatars;
    private int totPlayers = 0;

    private void putPlayer(String color)
    {
        avatars.get(totPlayers++).setImage(new Image("player/" + color.toLowerCase() + "_avatar.png"));
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


    public List<ImageView> getAvatars(){
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
