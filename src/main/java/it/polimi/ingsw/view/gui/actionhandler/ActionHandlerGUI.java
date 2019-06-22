package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerGUI extends ActionHandler implements Ifxml<Pane> {
    @FXML private StackPane killShotTrackPane;
    @FXML private StackPane playerPane;
    @FXML private StackPane mapPane;
    @FXML private Pane gameBoard;
    @FXML private VBox avatarsVBox;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private List<ImageView> avatars = new ArrayList<>();
    private PlayerCardsController cardsController;
    private MapController mapController;
    private KillShotTrackController killShotTrackController;

    public void initialize() {
        getRoot().setDisable(true);
        for(PlayerColor color : PlayerColor.values())
        {
            PlayerSetController playerSetController = PlayerSetController.getController(color);
            ImageView avatar = newAvatar(color, playerSetController);
            playerSetController.getRoot().minHeightProperty().bind(gameBoard.minHeightProperty().divide(5));
            playerSetController.getRoot().maxHeightProperty().bind(gameBoard.minHeightProperty().divide(5));
            playerSets.add(playerSetController);
            avatars.add(avatar);
            avatarsVBox.getChildren().add(avatar);
        }
        //blackRectangle.setVisible(false);
        cardsController = PlayerCardsController.getController();
        cardsController.getRoot().minHeightProperty().bind(gameBoard.minHeightProperty().divide(4.7));
        cardsController.getRoot().maxHeightProperty().bind(gameBoard.minHeightProperty().divide(4.7));
        mapController = MapController.getController();
        mapController.getRoot().minHeightProperty().bind(mapPane.minHeightProperty());
        mapController.getRoot().maxHeightProperty().bind(mapPane.minHeightProperty());
        mapPane.getChildren().add(mapController.getRoot());

        PlayerCardsController var = PlayerCardsController.getController();
        var.getRoot().minHeightProperty().bind(playerPane.minHeightProperty());
        var.getRoot().maxHeightProperty().bind(playerPane.minHeightProperty());
        playerPane.getChildren().add(var.getRoot());

        killShotTrackController = KillShotTrackController.getController();
        killShotTrackController.getRoot().minHeightProperty().bind(killShotTrackPane.minHeightProperty());
        killShotTrackController.getRoot().maxHeightProperty().bind(killShotTrackPane.minHeightProperty());
        killShotTrackPane.getChildren().add(killShotTrackController.getRoot());
        getRoot().setDisable(false);
        /*
        for(PlayerSetController controller : playerSets)
            playerSetsVBox.getChildren().add(controller.getRoot());

         */
    }

    private ImageView newAvatar(PlayerColor color, PlayerSetController playerSet)
    {
        ImageView avatar = new ImageView(new Image("player/" + color.getName() + "_avatar.png"));
        avatar.getStyleClass().add("button");
        avatar.fitHeightProperty().bind(gameBoard.heightProperty().multiply(0.9).divide(7));
        avatar.setPreserveRatio(true);

        avatar.setOnMouseEntered(e ->
        {
            Pane box;
            if(!gameBoard.getChildren().contains(playerSet.getRoot())) {
                box = playerSet.getRoot();
                onAvatarMouseOver(e, box, avatar, color);
            }
        });
        avatar.setOnMouseExited(e ->
        {
            gameBoard.getChildren().remove(playerSet.getRoot());
            gameBoard.getChildren().remove(cardsController.getRoot());
        });

        avatar.setOnMouseClicked(e -> {
            if(gameBoard.getChildren().remove(playerSet.getRoot()))
                onAvatarMouseOver(e, cardsController.getRoot(), avatar, color);
            else {
                gameBoard.getChildren().remove(cardsController.getRoot());
                onAvatarMouseOver(e, playerSet.getRoot(), avatar, color);
            }
        });
        return avatar;
    }

    private void onAvatarMouseOver(MouseEvent e, Pane box, ImageView avatar, PlayerColor color)
    {
        box.setDisable(true);
        box.setOnMouseEntered(e2 -> {
            int a = 2;
            a++;
        });
        gameBoard.getChildren().add(box);
        double w = box.getMaxWidth();
        box.setLayoutX(e.getSceneX() - e.getX() - 1.10*w);
        box.setLayoutY(e.getSceneY() - e.getY() - avatar.getFitHeight()/2.3);
    }


    @Override
    public Pane getRoot() {
        return gameBoard;
    }

    public static ActionHandlerGUI getController(){
        return GUIView.getController("/view/ActionHandler/general/actionHandler.fxml", "/view/ActionHandler/general/actionHandler.css");
    }

    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
        //TODO
    }

    @Override
    public void updateActionData(RemoteAction.Data data) throws IOException {
        //TODO
    }


    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }
}
