package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class MapController implements Ifxml<AnchorPane>
{
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
        mapImage.setImage(new Image("map0Test.png"));
        initializeSquares();
    }

     private void buildController(MatchSnapshotProvider provider){
        this.provider = provider;
        provider.modelChangedEvent().addEventHandler((a, snapshot) -> onModelChanged(snapshot));
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

    public void reset(){
        for(int i=0; i < squareControllers.length; i++)
            for(int j=0; j < squareControllers[i].length; j++)
                squareControllers[i][j].reset();
    }

    @Override
    public AnchorPane getRoot() {
        return mapGrid;
    }

    public static MapController getController(MatchSnapshotProvider provider){
        MapController ret = GUIView.getController("/view/ActionHandler/map/map.fxml", "/view/ActionHandler/map/map.css");
        ret.buildController(provider);
        return ret;
    }
}
