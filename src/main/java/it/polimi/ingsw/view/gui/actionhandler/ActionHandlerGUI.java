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
import it.polimi.ingsw.view.gui.RemoteActionsProvider;
import javafx.application.Platform;
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
import java.util.stream.Collectors;

import static it.polimi.ingsw.view.gui.actionhandler.PlayerCardsController.snapshotToName;

public class ActionHandlerGUI extends ActionHandler implements Ifxml<Pane>, MatchSnapshotProvider, RemoteActionsProvider {
    private final Event<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent = new Event<>();
    private final Event<RemoteActionsProvider, List<RemoteAction>> newActionsEvent = new Event<>();
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
    private String playerColor;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private List<PlayerCardsController> curCardsController;
    private PlayerSetController curPlayerSet;
    private PlayerCardsController playerCardsController;
    private List<ImageView> avatars = new ArrayList<>();
    private MapController mapController;
    private KillShotTrackController killShotTrackController;
    private SelectionBoxController redSelectionBoxController;
    private ColorAdjust mapBlurEffect = new ColorAdjust(0, -0.3, -0.8, 0);
    private List<RemoteAction> curActions;
    private RemoteAction curAction;
    private MatchSnapshot curSnapshot;
    private List<String> opponentsColors = new ArrayList<>();

    private static final double AVATAR_SCALE = 0.33;
    private static final double PLAYER_SET_SCALE = 1d/5.2;

    public void initialize() {

        //blackRectangle.setVisible(false);
        insert(MapController.getController(this).getRoot(), mapPane, 1);
        insert(KillShotTrackController.getController().getRoot(), killShotTrackPane,1);

        //redSelectionBoxController = ShopController.getController();
        //insert(redSelectionBoxController.getRoot(), mapOutPane, 0.5);
        //redSelectionBoxController.getRoot().setVisible(false);
        //setupStores();

        //todo remove this line
        //newMatch();
    }

    private void start(String playerColor, List<String> opponentColors)
    {
        this.playerColor = playerColor;
        this.opponentsColors = opponentColors;
        insert(curPlayerAvatar(playerColor), playerAvatarHBox, 0.6);
        for(String color : opponentColors)
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
           if(redSelectionBoxController.getRoot().isVisible())
           {
               mapPane.setDisable(false);
               setMapBlur(false);
               redSelectionBoxController.getRoot().setVisible(false);
           }
           else
           {
               mapPane.setDisable(true);
               setMapBlur(true);
               redSelectionBoxController.getRoot().setVisible(true);
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

    private void bindH(Pane toBind, Pane region, double wScaleFactor){
        toBind.minWidthProperty().bind(region.widthProperty().multiply(wScaleFactor));
        toBind.maxWidthProperty().bind(region.widthProperty().multiply(wScaleFactor));
    }
    private void bind(Pane toBind, Pane region, double hScaleFactor)
    {
        toBind.minHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
        toBind.maxHeightProperty().bind(region.minHeightProperty().multiply(hScaleFactor));
    }

    private void insertH(Pane toInsert, Pane region, double wScaleFactor){
        bindH(toInsert, region, wScaleFactor);
        region.getChildren().add(toInsert);
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

    private ImageView curPlayerAvatar(String color)
    {
        ImageView avatar = new ImageView(new Image("player/" + color + "_avatar.png"));
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

    private ImageView newAvatar(String color, PlayerSetController playerSet, PlayerCardsController cardsController)
    {
        ImageView avatar = new ImageView(new Image("player/" + color + "_avatar.png"));
        avatar.getStyleClass().add("button");
        avatar.setOnMouseEntered(e ->
        {
            Pane box;
            if(!gameBoard.getChildren().contains(playerSet.getRoot())) {
                box = playerSet.getRoot();
                onAvatarMouseOver(e, box, avatar);
            }
        });
        avatar.setOnMouseExited(e ->
        {
            gameBoard.getChildren().remove(playerSet.getRoot());
            gameBoard.getChildren().remove(cardsController.getRoot());
        });

        avatar.setOnMouseClicked(e -> {
            if(gameBoard.getChildren().remove(playerSet.getRoot()))
                onAvatarMouseOver(e, cardsController.getRoot(), avatar);
            else {
                gameBoard.getChildren().remove(cardsController.getRoot());
                onAvatarMouseOver(e, playerSet.getRoot(), avatar);
            }
        });
        return avatar;
    }

    private void onAvatarMouseOver(MouseEvent e, Pane box, ImageView avatar)
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
        List<PlayerColor> colors = new ArrayList<>(Arrays.asList(PlayerColor.GREY, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.VIOLET, PlayerColor.YELLOW));
        Match match = new Match(names, colors, 5, 0);
        match.start();
        List<Action> actions = match.getActions();
        List<RemoteAction> remoteActions = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            remoteActions.add(new RemoteAction(i, actions.get(i).getVisualizable()));
        visualizeActions(remoteActions);
        onModelChanged(new MatchSnapshot(match, match.getPlayer()));
    }

    private void visualizeActions(List<RemoteAction> actions){
        actionVBox.getChildren().clear();
        List<RemoteAction> imgActions = new ArrayList<>();
        for(RemoteAction action : actions)
            if(action.visualizable.imgPath != null){
                imgActions.add(action);
            }
            else{
                Button actionBtn = new Button(action.visualizable.name);
                actionBtn.setOnAction(e -> onActionChosen(action));
                actionVBox.getChildren().add(actionBtn);
            }
        if(imgActions.isEmpty())
            return;
        SelectionBoxController selectionBoxController = SelectionBoxController.getController(imgActions);

        insertH(selectionBoxController.getRoot(), mapOutPane, 0.4);
        setMapBlur(true);
        List<ImageView> options = selectionBoxController.getOptions();
        for(int i=0; i < options.size(); i++)
        {
            int i2 = i;
            options.get(i).setOnMouseClicked(e -> {
                mapOutPane.getChildren().remove(selectionBoxController.getRoot());
                onActionChosen(imgActions.get(i2));
            });
        }
    }

    private void onActionChosen(RemoteAction action)
    {
        curAction = action;
        notifyChoice(action.choose());
        notifyChoice(action.askActionData());
        setMapBlur(false);
    }


    private void setupAction(RemoteAction action){
        setupCards(action);
    }

    /**
     * Gets a list of possible actions and displays them to the user.
     * @param options Is the list of actions available the user can choose from
     */
    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
       Platform.runLater(() -> visualizeActions(options));
    }

    @Override
    public void updateActionData(RemoteAction.Data data) throws IOException {
        Platform.runLater(() -> {
        curAction.updateData(data);
        setupAction(curAction);});
    }

    private void setupCards(RemoteAction action)
    {
        setupWeapons(action);
        setupPowerups(action);
    }

    private void setupWeapons(RemoteAction action)
    {
        List<ImageView> weapons = playerCardsController.getWeapons();
        for(ImageView card : weapons)
            card.setDisable(true);
        for(String weapon : action.getData().getPossibleWeapons())
            for(ImageView weaponImg : weapons)
                if(weaponImg.getImage().getUrl().toLowerCase().contains(snapshotToName(weapon))) {
                    weaponImg.setDisable(false);
                    weaponImg.setOnMouseClicked(e -> notifyChoice(action.addWeapon(weapon)));
                }
    }

    private void setupPowerups(RemoteAction action)
    {
        List<ImageView> powerups = playerCardsController.getPowerUps();
        for(ImageView card : powerups)
            card.setDisable(true);
        for(String powerup : action.getData().getPossibleSquares())
            for(ImageView weaponImg : powerups)
                if(weaponImg.getImage().getUrl().toLowerCase().contains(snapshotToName(powerup))) {
                    weaponImg.setDisable(false);
                    weaponImg.setOnMouseClicked(e -> notifyChoice(action.usePowerUp(powerup)));
                }
    }

    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        if(playerColor == null)
            start(matchSnapshot.privatePlayerSnapshot.color, matchSnapshot.getPublicPlayerSnapshot().stream().map(p -> p.color).collect(Collectors.toList()));
        modelChangedEvent.invoke(this, curSnapshot = matchSnapshot);
    }

    @Override
    public MatchSnapshot getMatchSnapshot() {
        return curSnapshot;
    }

    @Override
    public IEvent<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent() {
        return modelChangedEvent;
    }

    @Override
    public List<RemoteAction> getActions() {
        return curActions;
    }

    @Override
    public IEvent<RemoteActionsProvider, List<RemoteAction>> newActionsEvent() {
        return newActionsEvent;
    }
}
