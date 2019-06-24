package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionHandlerGUI extends ActionHandler implements Ifxml<Pane>, MatchSnapshotProvider {
    public final IEvent<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent = new Event<>();
    @FXML private HBox playerHBox;
    @FXML private StackPane killShotTrackPane;
    @FXML private StackPane mapOutPane;
    @FXML private StackPane mapPane;
    @FXML private Pane gameBoard;
    @FXML private VBox avatarsVBox;
    @FXML private ImageView blueShop;
    @FXML private ImageView redShop;
    @FXML private ImageView yellowShop;
    @FXML private HBox playerAvatarHBox;
    @FXML private VBox actionVBox;
    private PlayerColor playerColor;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private List<PlayerCardsController> curCardsController;
    private PlayerSetController curPlayerSet;
    private PlayerCardsController playerCardsController;
    private List<ImageView> avatars = new ArrayList<>();
    private MapController mapController;
    private KillShotTrackController killShotTrackController;
    private ShopController redShopController;
    private ColorAdjust mapBlurEffect = new ColorAdjust(0, -0.3, -0.8, 0);
    private RemoteAction curAction;
    MatchSnapshot curSnapshot;
    private static final double AVATAR_SCALE = 0.33;
    private static final double PLAYER_SET_SCALE = 1d/5.2;

    public void initialize() {
        start(PlayerColor.BLUE);
        getRoot().setDisable(true);
        //blackRectangle.setVisible(false);
        insert(MapController.getController().getRoot(), mapPane, 1);
        insert(curPlayerAvatar(PlayerColor.YELLOW), playerAvatarHBox, 0.6);
        insert(KillShotTrackController.getController().getRoot(), killShotTrackPane,1);

        redShopController = ShopController.getController();
        insert(redShopController.getRoot(), mapOutPane, 0.5);
        redShopController.getRoot().setVisible(false);
        setupStores();
        getRoot().setDisable(false);

        //todo remove this line
        newMatch();
    }

    private void start(PlayerColor playerColor){

        this.playerColor = playerColor;
        for(PlayerColor color : PlayerColor.values())
            if(color != playerColor)
            {
                PlayerSetController playerSetController = PlayerSetController.getController(color, this);
                PlayerCardsController cardsController = PlayerCardsController.getController(this, color);
                bind(cardsController.getRoot(), mapPane, 0.3);
                ImageView avatar = newAvatar(color, playerSetController, cardsController);
                bind(playerSetController.getRoot(), gameBoard, PLAYER_SET_SCALE);
                playerSets.add(playerSetController);
                avatars.add(avatar);
                insert(avatar, avatarsVBox, AVATAR_SCALE);
            }
        playerCardsController = PlayerCardsController.getController(this, playerColor);
        curPlayerSet = PlayerSetController.getController(playerColor, this);
        bind(playerCardsController.getRoot(), gameBoard, PLAYER_SET_SCALE);
        insert(playerCardsController.getRoot(), playerHBox, 1);
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
            GaussianBlur blur = new GaussianBlur(55);
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
                insert(playerCardsController.getRoot(), playerHBox, 1);
            else {
                playerHBox.getChildren().remove(playerCardsController.getRoot());
                insert(curPlayerSet.getRoot(), playerHBox, 1);
            }
        });
        return avatar;
    }

    private ImageView newAvatar(PlayerColor color, PlayerSetController playerSet, PlayerCardsController cardsController)
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
        double w = box.getMinWidth();
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

    //todo remove this method! (only for testing)
    //todo
    //todo
    //todo
    //todo
    private void newMatch(){

        List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.GREY, PlayerColor.VIOLET, PlayerColor.YELLOW));
        Match match = new Match(names, colors, 5, 0);
        match.start();
        List<Action> actions = match.getActions();
        List<RemoteAction> remoteActions = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            remoteActions.add(new RemoteAction(i, actions.get(i).getText()));
        visualizeActions(remoteActions);
        onModelChanged(new MatchSnapshot(match, match.getPlayer()));
    }

    private void visualizeActions(List<RemoteAction> actions){
        actionVBox.getChildren().clear();
        for(RemoteAction action : actions){
            Button actionBtn = new Button(action.text);
            actionVBox.getChildren().add(actionBtn);
            actionBtn.setOnAction(e -> {
                curAction = action;
                notifyChoice(action.choose());
                notifyChoice(action.askActionData());
            });
        }
    }

    private void setupAction(RemoteAction action, RemoteAction.Data actionData){

    }

    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
        visualizeActions(options);
    }

    @Override
    public void updateActionData(RemoteAction.Data data) throws IOException {
        setupAction(curAction, data);
    }


    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        ((Event<MatchSnapshotProvider, MatchSnapshot>)modelChangedEvent).invoke(this, curSnapshot = matchSnapshot);
    }

    @Override
    public MatchSnapshot getMatchSnapshot() {
        return curSnapshot;
    }

    @Override
    public IEvent<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent() {
        return modelChangedEvent;
    }
}
