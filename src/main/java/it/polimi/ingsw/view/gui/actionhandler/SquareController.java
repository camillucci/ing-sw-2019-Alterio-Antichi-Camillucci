package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.snapshots.SquareSnapshot;
import it.polimi.ingsw.view.gui.Animations;
import it.polimi.ingsw.view.gui.Cache;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.Arrays;
import java.util.List;

public class SquareController implements Ifxml<StackPane> {
    @FXML private Circle circle;
    @FXML private ImageView ammoCardShop;
    @FXML private StackPane squareRootPane;
    @FXML private Avatar avatar1;
    @FXML private Avatar avatar2;
    @FXML private Avatar avatar3;
    @FXML private Avatar avatar4;
    @FXML private Avatar avatar5;
    private List<Avatar> avatars;
    private int totPlayers = 0;
    private ScaleTransition waitingTransition = null;
    private SquareSnapshot old;

    private void putPlayer(String color)
    {
        avatars.get(totPlayers++).setColor(color);
    }

    public void initialize(){
        avatars = getAvatars();
    }

    private String nameToUrl(String text){
        return "/ammo/" + text.replace(" ", "_").concat(".png").toLowerCase();
    }

    private void clearAvatars(){
        totPlayers = 0;
        for(ImageView avatar : avatars)
            avatar.setImage(null);
    }

    public void onModelChanged(SquareSnapshot square){
        if(square == null)
            return;

        clearAvatars();

        for(String color : square.getColors())
        {
            putPlayer(color);
            if(old == null || !old.getColors().contains(color))
                Animations.appearAnimation(getAvatars().get(totPlayers-1));
        }

        if(square.ammoSquare && square.getCards().size() == 1)
        {
            ammoCardShop.setImage(Cache.getImage(nameToUrl(square.getCards().get(0).toLowerCase())));
            if(old == null || !old.ammoSquare || old.getCards().size() != 1)
                Animations.appearAnimation(ammoCardShop);
        }
        else if(old != null && old.ammoSquare && old.getCards().size() == 1)
            Animations.disappearAnimation(ammoCardShop, () -> ammoCardShop.setImage(null));
        old = square;
    }


    public void startWaitingAnimation(String color)
    {
        for(Avatar a: avatars)
            if(a.getColor() != null && a.getColor().equals(color))
                startWaitingAnimation(a);
    }

    public void startWaitingAnimation(Avatar avatar)
    {
        Animations.zoomAnimation(avatar, 1.5, 1.5);
    }

    public void setClickable(EventHandler<MouseEvent> eventHandler){
        squareRootPane.getStyleClass().add("rootPane");
        squareRootPane.setOnMouseEntered(e -> circle.setVisible(true));
        squareRootPane.setOnMouseExited(e -> circle.setVisible(false));
        this.squareRootPane.setOnMouseClicked(eventHandler);
    }

    public void setClickable(Avatar avatar, EventHandler<MouseEvent> eventHandler){
        avatar.getStyleClass().add("button");
        avatar.setOnMouseClicked(eventHandler);
    }

    public void reset(){
        squareRootPane.getStyleClass().remove("rootPane");
        this.circle.setVisible(false);
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
