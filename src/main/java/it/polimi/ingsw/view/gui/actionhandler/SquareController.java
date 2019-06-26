package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SquareController implements Ifxml<StackPane> {
    @FXML private Circle circle;
    @FXML private ImageView ammoCard_shop;
    @FXML private StackPane squareRootPane;
    @FXML private Avatar avatar1;
    @FXML private Avatar avatar2;
    @FXML private Avatar avatar3;
    @FXML private Avatar avatar4;
    @FXML private Avatar avatar5;
    private List<Avatar> avatars;
    private int totPlayers = 0;
    private ScaleTransition waitingTransition = null;

    private void putPlayer(String color)
    {
        avatars.get(totPlayers++).setColor(color);
    }

    public void initialize(){
        avatars = getAvatars();
    }

    private String nameToUrl(String text){
        String ret = "ammo/" + text.replace(" ", "_").concat(".png").toLowerCase();
        return ret;
    }

    private void clear(){
        totPlayers = 0;
        for(ImageView avatar : avatars)
            avatar.setImage(null);
        ammoCard_shop.setImage(null);
    }

    public void onModelChanged(SquareSnapshot square){
        if(square == null)
            return;

        List<String> backup = avatars.stream().map(Avatar::getColor).collect(Collectors.toList());
        clear();
        for(String color : square.getColors())
            putPlayer(color);
        List<Avatar> players = avatars.stream().filter(a -> a.getColor() != null).collect(Collectors.toList());
        for(int i=0; i < players.size(); i++)
            if(backup.size() <= i || !avatars.get(i).getColor().equals(backup.get(i)))
                startWaitingAnimation(avatars.get(i));
        if(square.ammoSquare && square.getCards().size() == 1)
            ammoCard_shop.setImage(new Image(nameToUrl(square.getCards().get(0))));

    }

    private void startWaitingAnimation(Avatar avatar)
    {
        waitingTransition = new ScaleTransition();
        waitingTransition.setDuration(Duration.millis(200));
        waitingTransition.setNode(avatar);
        waitingTransition.setByY(1.5);
        waitingTransition.setByX(1.5);
        waitingTransition.setCycleCount(2);
        waitingTransition.setAutoReverse(true);
        waitingTransition.play();
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
