package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.endgame.EndGameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extends ActionHandler and is dedicated to managing actions on clients side, in case the user chose a
 * GUI based display.
 */
public class ActionHandlerGUI extends ActionHandler implements Ifxml<Pane>, MatchSnapshotProvider, RemoteActionsProvider {
    private final Event<MatchSnapshotProvider, MatchSnapshot> modelChangedEvent = new Event<>();
    private final Event<RemoteActionsProvider, RemoteAction> newActionsEvent = new Event<>();
    private final Event<RemoteActionsProvider, Object> notifyingToServerEvent = new Event<>();
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
    private ShopController redShopController;
    private ShopController blueShopController;
    private ShopController yellowShopController;
    private String playerColor;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private List<PlayerCardsController> curCardsController = new ArrayList<>();
    private List<AmmoBoxController> ammoBoxControllers = new ArrayList<>();
    private List<Avatar> avatars = new ArrayList<>();
    private PlayerSetController curPlayerSet;
    private PlayerCardsController playerCardsController;
    private AmmoBoxController playerAmmoBoxController;
    private List<HBox> avatarBoxes = new ArrayList<>();
    private MapController mapController;
    private KillShotTrackController killShotTrackController;
    private ColorAdjust mapBlurEffect = new ColorAdjust(0, -0.3, -0.8, 0);
    private List<RemoteAction> curActions;
    private RemoteAction curAction;
    private MatchSnapshot curSnapshot;
    private List<String> opponentsColors = new ArrayList<>();
    private static final double AVATAR_SCALE = 0.3;
    private static final double PLAYER_SET_SCALE = 1d/5.2;
    private static final double SELECTION_BOX_SCALE = 0.5;
    private SelectionBoxController selectionBoxController;
    private List<ShopController> shops;
    private ImageView rollBack;
    private List<String> disconnectedPlayers = new ArrayList<>();

    public void initialize() {
        (redShopController = ShopController.getController(this, "red")).getRoot().setVisible(false);
        (blueShopController = ShopController.getController(this, "blue")).getRoot().setVisible(false);
        (yellowShopController = ShopController.getController(this, "yellow")).getRoot().setVisible(false);
        shops = setupStores();

        mapController = MapController.getController(this, this);
        killShotTrackController = KillShotTrackController.getController(this);

        insertH(redShopController.getRoot(), mapOutPane, SELECTION_BOX_SCALE);
        insertH(blueShopController.getRoot(), mapOutPane, SELECTION_BOX_SCALE);
        insertH(yellowShopController.getRoot(), mapOutPane, SELECTION_BOX_SCALE);
        insert(mapController.getRoot(), mapPane, 1);
        insert(killShotTrackController.getRoot(), killShotTrackPane,1);
    }

    /**
     * lunches a series of methods that provides the initial user interface for when the game starts
     * @param playerColor color the user has chosen
     * @param opponentColors color of all the other users
     */
    private void start(String playerColor, List<String> opponentColors)
    {
        this.playerColor = playerColor;
        this.opponentsColors = opponentColors;
        playerAmmoBoxController = AmmoBoxController.getController(playerColor, this, this);

        insert(curPlayerAvatar(playerColor, playerAmmoBoxController), playerAvatarHBox, 0.6);
        for(String color : opponentColors)
        {
            PlayerSetController playerSetController = PlayerSetController.getController(color, this);
            PlayerCardsController cardsController = PlayerCardsController.getController(this, color);
            AmmoBoxController ammoBoxController = AmmoBoxController.getController(color, this);

            playerSets.add(playerSetController);
            curCardsController.add(cardsController);
            ammoBoxControllers.add(ammoBoxController);

            bind(cardsController.getRoot(), mapPane, 0.3);
            HBox avatarHBox = newAvatarHBox(color, playerSetController, cardsController, ammoBoxController);
            bind(playerSetController.getRoot(), gameBoard, PLAYER_SET_SCALE);
            avatarBoxes.add(avatarHBox);

            insert(avatarHBox, avatarsVBox, AVATAR_SCALE);
        }
        playerCardsController = PlayerCardsController.getController(this, this, playerColor);
        curPlayerSet = PlayerSetController.getController(playerColor, this);
        bind(playerCardsController.getRoot(), gameBoard, PLAYER_SET_SCALE);
        insert(playerCardsController.getRoot(), playerHBox, 1);
        setupEvents();
    }

    /**
     * Subscribes to all the events invoked when a user makes a choices (selects a target, chooses to use a weapon, etc)
     */
    private void setupEvents() {
        playerCardsController.addWeaponEvent.addEventHandler((a,weapon) -> notifyAndReset(getAction().addWeapon(weapon)));
        playerCardsController.usePowerupEvent.addEventHandler((a,pu) -> notifyAndReset(getAction().usePowerUp(pu)));
        playerCardsController.addPowerupEvent.addEventHandler((a,pu) -> notifyAndReset(getAction().addDiscardable(pu)));
        playerAmmoBoxController.addDiscardedAmmoEvent.addEventHandler((a,ammo) -> notifyAndReset(getAction().addDiscardableAmmo(ammo)));
        mapController.addTargetSquareEvent.addEventHandler((a,square) -> notifyAndReset(getAction().addTargetSquare(square)));
        mapController.addPTargetPlayerEvent.addEventHandler((a, player) -> notifyAndReset(getAction().addTargetPlayer(player)));
    }

    /**
     * calls setupShop method for every shop (3 in total)
     * @return The list of controllers relative to the three newly created shop icons
     */
    private List<ShopController> setupStores(){
        setupShop(redShop, redShopController);
        setupShop(blueShop, blueShopController);
        setupShop(yellowShop, yellowShopController);
        return Arrays.asList(redShopController, blueShopController, yellowShopController);
    }

    /**
     * Displays an icon corresponding to the store gotten has a parameter. The icon is clickable and displays
     * the weapons it currently contains when the user clicks on it.
     * @param store Icon representing one of the 3 stores.
     * @param shopController Controller relative to the shop icon the user is approaching
     */
    private void setupShop(ImageView store, ShopController shopController){
        store.setOnMouseClicked(e -> {
           if(shops.stream().anyMatch(shop -> shop.getRoot().isVisible()))
           {
               showMap();
               shops.forEach(shop -> shop.getRoot().setVisible(false));
           }
           else
           {
               hideMap();
               shopController.getRoot().setVisible(true);
           }
        });
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

    private void insertH(ImageView toInsert, Pane region, double wScaleFactor){
        toInsert.fitWidthProperty().bind(region.widthProperty().multiply(wScaleFactor));
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


    private HBox createAvatarHBox()
    {
        final double SPACING_FACTOR = 0.1;
        HBox ret = new HBox();
        ret.setAlignment(Pos.CENTER);
        ret.spacingProperty().bind(ret.minHeightProperty().multiply(SPACING_FACTOR));
        return ret;
    }


    private HBox curPlayerAvatar(String color, AmmoBoxController ammoBoxController)
    {
        HBox ret = createAvatarHBox();
        Avatar avatar = new Avatar(color);
        avatars.add(avatar);
        avatar.getStyleClass().add("button");

        avatar.setOnMouseClicked(e -> {
            if(playerHBox.getChildren().remove(curPlayerSet.getRoot()))
                insert(playerCardsController.getRoot(), playerHBox, 1);
            else {
                playerHBox.getChildren().remove(playerCardsController.getRoot());
                insert(curPlayerSet.getRoot(), playerHBox, 1);
            }
        });
        insert(ammoBoxController.getRoot(), ret, 1);
        insert(avatar, ret, 1);
        return ret;
    }

    private HBox newAvatarHBox(String color, PlayerSetController playerSet, PlayerCardsController cardsController, AmmoBoxController ammoBoxController)
    {
        HBox ret = createAvatarHBox();
        Avatar avatar = new Avatar(color);
        avatars.add(avatar);
        avatar.getStyleClass().add("button");
        gameBoard.getChildren().add(playerSet.getRoot());
        gameBoard.getChildren().add(cardsController.getRoot());
        playerSet.getRoot().setVisible(false);
        cardsController.getRoot().setVisible(false);
        avatar.setOnMouseEntered(e -> onAvatarMouseOver(e, playerSet.getRoot(), avatar));
        avatar.setOnMouseExited(e ->
        {
            playerSet.getRoot().setVisible(false);
            cardsController.getRoot().setVisible(false);
        });

        avatar.setOnMouseClicked(e -> {
            if(playerSet.getRoot().isVisible())
            {
                playerSet.getRoot().setVisible(false);
                onAvatarMouseOver(e, cardsController.getRoot(), avatar);
            }
            else {
                cardsController.getRoot().setVisible(false);
                onAvatarMouseOver(e, playerSet.getRoot(), avatar);
            }
        });
        insert(avatar, ret, 1);
        insert(ammoBoxController.getRoot(), ret, 1);
        return ret;
    }

    private void onAvatarMouseOver(MouseEvent e, Pane box, ImageView avatar)
    {
        box.setVisible(true);
        double w = box.getMinWidth();
        box.setLayoutX(e.getSceneX() -e.getX() + 0.2*w);
        box.setLayoutY(e.getSceneY() - e.getY() - avatar.getFitHeight()/2);
    }

    @Override
    public Pane getRoot() {
        return gameBoard;
    }

    public static ActionHandlerGUI getController(){
        return GUIView.getController("/view/ActionHandler/general/actionHandler.fxml", "/view/ActionHandler/general/actionHandler.css");
    }

    private void clearAnd(boolean removeRollback, Runnable func){
        clear(removeRollback);
        (new Thread(func)).start();
    }


    private void clear(boolean removeRollback){
        showMap();
        mapController.reset();
        actionVBox.getChildren().clear();
        if(!removeRollback)
            actionVBox.getChildren().add(rollBack);
    }

    private void showMap()
    {
        if(selectionBoxController!= null)
            mapOutPane.getChildren().remove(selectionBoxController.getRoot());
        mapPane.setEffect(null);
        shops.forEach(shop -> shop.getRoot().setVisible(false));
        mapPane.setDisable(false);
    }

    private void hideMap(){
        mapPane.setDisable(true);
        GaussianBlur blur = new GaussianBlur(55);
        mapBlurEffect.setInput(blur);
        mapPane.setEffect(mapBlurEffect);
    }

    private void visualizeActions(List<RemoteAction> actions)
    {
        clear(true);
        if(actions.isEmpty())
            return;
        List<RemoteAction> imgActions = new ArrayList<>();
        for(RemoteAction action : actions)
            if(action.visualizable.imgPath != null)
                imgActions.add(action);
            else
               rollBack = newButton(action.visualizable, () -> onActionChosen(action));

        if(!imgActions.isEmpty())
            setupImgAction(imgActions);
    }

    private void setupImgAction(List<RemoteAction> imgActions)
    {
        selectionBoxController = SelectionBoxController.getController(imgActions);
        insertH(selectionBoxController.getRoot(), mapOutPane, SELECTION_BOX_SCALE);
        List<ImageView> options = selectionBoxController.getOptions();
        for(int i=0; i < options.size(); i++)
        {
            int i2 = i;
            options.get(i).setOnMouseClicked(e -> onActionChosen(imgActions.get(i2)));
        }
        showSelectionBox();
    }

    private void showSelectionBox() {
        hideMap();
        selectionBoxController.getRoot().setVisible(true);
    }

    @Override
    public void onTurnStart()
    {
        mapController.startWaitingAnimation(playerColor);
    }

    private void onActionChosen(RemoteAction action)
    {
        curAction = action;
        showMap();
        clearAnd(false, () -> {
            notifyChoice(action.choose());
            notifyChoice(action.askActionData());
        });
    }

    /**
     * Gets a list of possible actions and displays them to the user.
     * @param options Is the list of actions available the user can choose from
     */
    @Override
    public void chooseAction(List<RemoteAction> options){
       Platform.runLater(() -> visualizeActions(options));
    }

    @Override
    public void updateActionData(RemoteAction.Data data) {
        Platform.runLater(() -> {
            curAction.updateData(data);
            setupAction(curAction);
        });
    }

    private void setupAction(RemoteAction action)
    {
        newActionsEvent.invoke(this, action);

        if(action.getData().canBeDone)
            if(checkForAutoDoAction(action))
                doActionAndReset();
            else
                newButton(new Visualizable("Confirm"), () -> doActionAndReset());
    }

    private boolean checkForAutoDoAction(RemoteAction action)
    {
        RemoteAction.Data data = action.getData();
        return data.canBeDone &&
                data.getDiscardablePowerUps().isEmpty() && data.getPossiblePlayers().isEmpty() && data.getPossibleSquares().isEmpty()
                && data.getPossibleWeapons().isEmpty() && data.getDiscardableAmmos().isEmpty() &&data.getPossiblePowerUps().isEmpty();
    }

    private void doActionAndReset()
    {
        notifyingToServerEvent.invoke(this, null);
        clearAnd(true, () -> notifyChoice(curAction.doAction()));
    }

    private void notifyAndReset(Command<RemoteActionsHandler> command){
        notifyingToServerEvent.invoke(this, null);
        clearAnd(false, () -> {
            notifyChoice(command);
            notifyChoice(curAction.askActionData());
        });
    }

    private String nameToColor(MatchSnapshot snapshot, String name)
    {
        for(PublicPlayerSnapshot p : snapshot.getPublicPlayerSnapshot())
            if(p.name.equals(name))
                return p.color;
        return snapshot.privatePlayerSnapshot.color;
    }

    private Avatar getAvatar(String name){
        String color = nameToColor(curSnapshot, name);
        for(Avatar avatar : avatars)
            if(avatar.getColor().equals(color))
                return avatar;
        throw new RuntimeException("Avatar does not exist");
    }

    @Override
    public void onNewMessage(String message) {

    }

    @Override
    public void disconnectedPlayerMessage(String name) {
       Platform.runLater( () -> getAvatar(name).disconnected());
    }


    @Override
    public void reconnectedMessage(String name) {
        Platform.runLater( () -> getAvatar(name).reconnected());
    }

    @Override
    public void winnerMessage(String winner) {
        return;
    }

    @Override
    public void scoreboardMessage(String[][] scoreboard) {
        ((Event<ActionHandler, EndGameController.EndGameData>)matchEndedEvent).invoke(this, new EndGameController.EndGameData(scoreboard, curSnapshot));
    }

    private ImageView newButton(Visualizable visualizable, Runnable action)
    {
        if(visualizable.icon != null)
        {
            ImageView button = new ImageView(Cache.getImage("/" + visualizable.icon));
            button.setPreserveRatio(true);
            button.getStyleClass().add("button");
            button.setOnMouseClicked(e -> action.run());
            insert(button, actionVBox, 0.07);
            return button;
        }
        else
        {
            Button button = new Button(visualizable.name);
            button.setOnAction(e -> action.run());
            actionVBox.getChildren().add(button);
        }
        return null;
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        Platform.runLater(() -> {
        if(playerColor == null)
            start(matchSnapshot.privatePlayerSnapshot.color, matchSnapshot.getPublicPlayerSnapshot().stream().map(p -> p.color).collect(Collectors.toList()));
        modelChangedEvent.invoke(this, curSnapshot = matchSnapshot);});
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
    public RemoteAction getAction() {
        return curAction;
    }

    @Override
    public IEvent<RemoteActionsProvider, RemoteAction> newActionsEvent() {
        return newActionsEvent;
    }

    @Override
    public IEvent<RemoteActionsProvider, Object> notifyingToServeEvent() {
        return notifyingToServerEvent;
    }
}
