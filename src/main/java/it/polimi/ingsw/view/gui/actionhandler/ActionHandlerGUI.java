package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerGUI extends ActionHandler implements Ifxml<Pane> {
    @FXML private HBox playerHBox;
    @FXML private StackPane killShotTrackPane;
    @FXML private StackPane mapOutPane;
    @FXML private StackPane mapPane;
    @FXML private Pane gameBoard;
    @FXML private VBox avatarsVBox;
    @FXML private ImageView blueShop;
    @FXML private ImageView redShop;
    @FXML private ImageView yellowShop;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private List<ImageView> avatars = new ArrayList<>();
    private PlayerCardsController cardsController;
    private MapController mapController;
    private KillShotTrackController killShotTrackController;
    private PlayerSetController curPlayerSet;
    private PlayerCardsController curCardsController;
    private ShopController redShopController;

    ColorAdjust mapBlurEffect = new ColorAdjust(0, -0.3, -0.8, 0);
    private static final double AVATAR_SCALE = 0.1;
    private static final double PLAYER_SET_SCALE = 1d/5.2;

    public void initialize() {
        getRoot().setDisable(true);
        for(PlayerColor color : PlayerColor.values())
            if(color != PlayerColor.YELLOW)
            {
                PlayerSetController playerSetController = PlayerSetController.getController(color);
                ImageView avatar = newAvatar(color, playerSetController);
                bind(playerSetController.getRoot(), gameBoard, PLAYER_SET_SCALE);
                playerSets.add(playerSetController);
                avatars.add(avatar);
                insert(avatar, avatarsVBox, AVATAR_SCALE);
            }
        //blackRectangle.setVisible(false);
        curCardsController = PlayerCardsController.getController();
        curPlayerSet = PlayerSetController.getController(PlayerColor.YELLOW);
        cardsController = PlayerCardsController.getController();
        bind(cardsController.getRoot(), gameBoard, PLAYER_SET_SCALE);
        insert(MapController.getController().getRoot(), mapPane, 1);
        insert(curPlayerAvatar(PlayerColor.YELLOW), playerHBox, 0.6);
        insert(curCardsController.getRoot(), playerHBox, 1);
        insert(KillShotTrackController.getController().getRoot(), killShotTrackPane,1);

        redShopController = ShopController.getController();
        insert(redShopController.getRoot(), mapOutPane, 0.5);
        redShopController.getRoot().setVisible(false);
        setupStores();
        getRoot().setDisable(false);
    }

    private void setupStores(){
        setupShop(redShop);
        setupShop(blueShop);
        setupShop(yellowShop);
    }

    private void setupShop(ImageView store){
        store.setOnMouseClicked(e -> {
           if(redShopController.getRoot().isVisible())
           {
               mapPane.setDisable(false);
               setMapBlur(false);
               redShopController.getRoot().setVisible(false);
           }
           else
           {
               mapPane.setDisable(true);
               setMapBlur(true);
               redShopController.getRoot().setVisible(true);
           }
        });

    }

    private void setMapBlur(boolean blurBool){
        if(blurBool) {
            GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
            mapBlurEffect.setInput(blur);
            mapPane.setEffect(mapBlurEffect);
        }
        else
            mapPane.setEffect(null);

    }

    private void bind(ImageView toBind, Pane region, double hScaleFactor){
        toBind.fitHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
    }

    private void bind(Pane toBind, Pane region, double hScaleFactor)
    {
        toBind.minHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
        toBind.maxHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
    }

    private void insert(Pane toInsert, Pane region, double hScaleFactor)
    {
        bind(toInsert, region, hScaleFactor);
        region.getChildren().add(toInsert);
    }

    private void insert(ImageView toInsert, Pane region, double hScaleFactor)
    {
        toInsert.fitHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
        toInsert.setPreserveRatio(true);
        region.getChildren().add(toInsert);
    }

    private ImageView curPlayerAvatar(PlayerColor color)
    {
        ImageView avatar = new ImageView(new Image("player/" + color.getName() + "_avatar.png"));
        avatar.getStyleClass().add("button");

        avatar.setOnMouseClicked(e -> {
            if(playerHBox.getChildren().remove(curPlayerSet.getRoot()))
                insert(curCardsController.getRoot(), playerHBox, 1);
            else {
                playerHBox.getChildren().remove(curCardsController.getRoot());
                insert(curPlayerSet.getRoot(), playerHBox, 1);
            }
        });
        return avatar;
    }

    private ImageView newAvatar(PlayerColor color, PlayerSetController playerSet)
    {
        ImageView avatar = new ImageView(new Image("player/" + color.getName() + "_avatar.png"));
        avatar.getStyleClass().add("button");

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
        gameBoard.getChildren().add(box);
        double w = box.getMaxWidth();
        box.setLayoutX(e.getSceneX() - e.getX() - 1.10*w);
        box.setLayoutY(e.getSceneY() - e.getY() - avatar.getFitHeight()/2);
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
