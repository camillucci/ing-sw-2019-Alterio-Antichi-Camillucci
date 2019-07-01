package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import it.polimi.ingsw.view.gui.RemoteActionsProvider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class MapController implements Ifxml<AnchorPane>
{
    public final IEvent<MapController, String> addTargetSquareEvent = new Event<>();
    public final IEvent<MapController, String> addPTargetPlayerEvent = new Event<>();
    private static final int R = 3;
    private static final int C = 4;
    @FXML private StackPane square1;
    @FXML private StackPane square2;
    @FXML private StackPane square3;
    @FXML private StackPane square4;
    @FXML private StackPane square5;
    @FXML private StackPane square6;
    @FXML private StackPane square7;
    @FXML private StackPane square8;
    @FXML private StackPane square9;
    @FXML private StackPane square10;
    @FXML private StackPane square11;
    @FXML private StackPane square12;
    @FXML private ImageView mapImage;
    @FXML private AnchorPane mapGrid;
    private MatchSnapshotProvider provider;
    private SquareController squareController;
    private StackPane[][] squarePanes;
    private SquareController[][] squareControllers;
    private RemoteActionsProvider actionProvider;


    public StackPane[][] getSquarePanes()
    {
        if(squarePanes != null)
            return squarePanes;

        StackPane[][] ret = new StackPane[R][C];
        ret[0] = new StackPane[] {square1, square2, square3, square4};
        ret[1] = new StackPane[] {square5, square6, square7, square8};
        ret[2] = new StackPane[] {square9, square10, square11, square12};
        return ret;
    }

    public SquareController[][] getSquares(){
        return squareControllers;
    }

    public void initialize(){
        mapImage.setImage(new Image(getClass().getResourceAsStream("/map0Test.png")));
        initializeSquares();
    }

     private void buildController(MatchSnapshotProvider provider, RemoteActionsProvider actionsProvider){
        this.provider = provider;
        this.actionProvider = actionsProvider;
        provider.modelChangedEvent().addEventHandler((a, snapshot) -> onModelChanged(snapshot));
        actionsProvider.newActionsEvent().addEventHandler((a, action) -> setupSquares(action));
        actionsProvider.newActionsEvent().addEventHandler((a, action) -> setupPlayers(action));
     }

    private void setupSquares(RemoteAction action) {
        RemoteAction.Data data = action.getData();
        SquareSnapshot[][] squareSnapshots = provider.getMatchSnapshot().gameBoardSnapshot.squareSnapshots;
        for(String name : data.getPossibleSquares())
            for(int i=0; i < squareControllers.length; i++)
                for(int j=0; j < squareControllers[i].length; j++)
                    if(squareSnapshots[i][j] != null && squareSnapshots[i][j].name.equals(name))
                        squareControllers[i][j].setClickable(e -> invokeEvent(addTargetSquareEvent, name));
    }

    private void setupPlayers(RemoteAction action)
    {
        RemoteAction.Data data = action.getData();
        List<Avatar> avatars = new ArrayList<>();
        List<SquareController> controllers = new ArrayList<>();
        for(int i=0; i < squareControllers.length; i++)
            for(int j=0; j < squareControllers[0].length; j++)
                for(Avatar avatar : squareControllers[i][j].getAvatars())
                    if(data.getPossiblePlayers().contains(colorToName(avatar.getColor())))
                    {
                        avatars.add(avatar);
                        controllers.add(squareControllers[i][j]);
                    }
        for(int i=0; i < avatars.size(); i++) {
            int j = i;
            controllers.get(i).setClickable(avatars.get(i), e -> invokeEvent(addPTargetPlayerEvent, colorToName(avatars.get(j).getColor())));
        }
    }

    private String colorToName(String color){
        for(PublicPlayerSnapshot player : provider.getMatchSnapshot().getPublicPlayerSnapshot())
            if(player.color.equals(color))
                return player.name;
        return null; // impossible to go here
    }

    private <T> void invokeEvent(IEvent<MapController, T> event, T arg)
    {
        ((Event<MapController, T>)event).invoke(this, arg);
    }

    private void onModelChanged(MatchSnapshot snapshot) {
        for(int i = 0; i < R; i++)
            for(int j=0; j < C; j++)
                squareControllers[i][j].onModelChanged(snapshot.gameBoardSnapshot.squareSnapshots[i][j]);
    }

    private void initializeSquares(){
        squareControllers = new SquareController[R][C];
        squarePanes = getSquarePanes();
        for(int i=0; i < R; i++)
            for(int j=0; j < C; j++){
                SquareController tmp = SquareController.getController();
                tmp.getRoot().minHeightProperty().bind(square1.minHeightProperty());
                tmp.getRoot().maxHeightProperty().bind(square1.maxHeightProperty());
                tmp.getRoot().minWidthProperty().bind(square1.widthProperty());
                tmp.getRoot().maxWidthProperty().bind(square1.widthProperty());
                squareControllers[i][j] = tmp;
                squarePanes[i][j].getChildren().add(squareControllers[i][j].getRoot());
            }
    }

    public void startWaitingAnimation(String color)
    {
        for(int i=0; i < R; i++)
            for(int j=0; j < C; j++)
                squareControllers[i][j].startWaitingAnimation(color);
    }

    public void reset(){
        for(int i=0; i < squareControllers.length; i++)
            for(int j=0; j < squareControllers[i].length; j++)
                squareControllers[i][j].reset();
    }

    @Override
    public AnchorPane getRoot() {
        return mapGrid;
    }

    public static MapController getController(MatchSnapshotProvider provider, RemoteActionsProvider actionProvider){
        MapController ret = GUIView.getController("/view/ActionHandler/map/map.fxml", "/view/ActionHandler/map/map.css");
        ret.buildController(provider, actionProvider);
        return ret;
    }
}
