package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import it.polimi.ingsw.view.gui.RemoteActionsProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/**
 * This class extends ActionHandler and is dedicated to managing actions on clients side, in case the user chose a
 * GUI based display.
 */
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
        mapController = MapController.getController(this);
        killShotTrackController = KillShotTrackController.getController();

        insert(mapController.getRoot(), mapPane, 1);
        insert(killShotTrackController.getRoot(), killShotTrackPane,1);

        //todo remove this line
        //newMatch();
    }

    /**
     * lunches a series of methods that provide the initial user interface for when the game starts
     * @param playerColor color the user has chosen
     * @param opponentColors color of all the other users
     */
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

    /**
     * calls setupShop method for every shop (3 in total)
     */
    private void setupStores(){
        setupShop(redShop);
        setupShop(blueShop);
        setupShop(yellowShop);
    }

    /**
     * Displays an icon corresponding to the store gotten has a parameter. The icon is clickable and displays
     * the weapons it currently contains when the user clicks on it.
     * @param store Icon representing one of the 3 stores.
     */
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
    /*
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

     */



    private void visualizeActions(List<RemoteAction> actions){
        actionVBox.getChildren().clear();
        List<RemoteAction> imgActions = new ArrayList<>();
        for(RemoteAction action : actions)
            if(action.visualizable.imgPath != null){
                imgActions.add(action);
            }
            else
                newButton(action.visualizable.name, e -> onActionChosen(action));
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
            setupAction(curAction);
        });
    }

    private void setupAction(RemoteAction action){
        setupCards(action);
        setupSquares(action);
        setupPlayers(action);

        if(action.getData().canBeDone)
            newButton("Confirm", e -> {
                actionVBox.getChildren().clear();
                doActionAndReset();
            });
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
                    weaponImg.setOnMouseClicked(e -> notifyAndReset(action.addWeapon(weapon)));
                }
    }

    private void setupPowerups(RemoteAction action)
    {
        List<ImageView> powerups = playerCardsController.getPowerUps();
        for(ImageView card : powerups)
            card.setDisable(true);
        for(String powerup : action.getData().getDiscardablePowerUps())
            for(ImageView powerupImg : powerups)
                if(powerupImg.getImage() != null && powerupImg.getImage().getUrl().toLowerCase().contains(snapshotToName(powerup))) {
                    powerupImg.setDisable(false);
                    powerupImg.setOnMouseClicked(e -> notifyAndReset(action.usePowerUp(powerup)));
                }
    }

    private void setupSquares(RemoteAction action)
    {
        RemoteAction.Data data = action.getData();
        SquareController[][] squares = mapController.getSquares();
        SquareSnapshot[][] squareSnapshots = curSnapshot.gameBoardSnapshot.squareSnapshots;
        for(String name : data.getPossibleSquares())
            for(int i=0; i < squares.length; i++)
                for(int j=0; j < squares[i].length; j++)
                    if(squareSnapshots[i][j] != null && squareSnapshots[i][j].name.equals(name))
                        squares[i][j].setClickable(e -> notifyAndReset(action.addTargetSquare(name)));
    }

    private void setupPlayers(RemoteAction action)
    {
        RemoteAction.Data data = action.getData();
        SquareController[][] squares = mapController.getSquares();
        List<Avatar> avatars = new ArrayList<>();
        List<SquareController> squareControllers = new ArrayList<>();
        for(int i=0; i < squares.length; i++)
            for(int j=0; j < squares[0].length; j++)
                for(Avatar avatar : squares[i][j].getAvatars())
                    if(data.getPossiblePlayers().contains(colorToName(avatar.getColor())))
                    {
                        avatars.add(avatar);
                        squareControllers.add(squares[i][j]);
                    }
        for(int i=0; i < avatars.size(); i++) {
            int j = i;
            squareControllers.get(i).setClickable(avatars.get(i), e -> notifyAndReset(action.addTargetPlayer(colorToName(avatars.get(j).getColor()))));
        }

    }

    private void reset(){
        mapController.reset();
    }

    private void doActionAndReset(){
        mapController.reset();
        notifyChoice(curAction.doAction());
    }

    private void notifyAndReset(Command<RemoteActionsHandler> command){
        reset();
        notifyChoice(command);
        notifyChoice(curAction.askActionData());
    }

    // todo improve performance saving the result
    private String colorToName(String color){
        for(PublicPlayerSnapshot player : curSnapshot.getPublicPlayerSnapshot())
            if(player.color.equals(color))
                return player.name;
        return null; // impossible to go here
    }

    private String nameToColor(String name){
        for(PublicPlayerSnapshot player : curSnapshot.getPublicPlayerSnapshot())
            if(player.name.equals(name))
                return player.color;
        return null; // impossible to go here
    }

    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    private void newButton(String text, EventHandler<ActionEvent> handler){
        Button actionBtn = new Button(text);
        actionBtn.setOnAction(handler);
        actionVBox.getChildren().add(actionBtn);
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
    public List<RemoteAction> getActions() {
        return curActions;
    }

    @Override
    public IEvent<RemoteActionsProvider, List<RemoteAction>> newActionsEvent() {
        return newActionsEvent;
    }
}
