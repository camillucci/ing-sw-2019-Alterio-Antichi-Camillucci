package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerGUI extends ActionHandler implements Ifxml<GridPane> {
    @FXML private GridPane gameBoard;
    @FXML private VBox playerSetsVBox;
    private Scene actionHandlerScene;
    private List<PlayerSetController> playerSets = new ArrayList<>();
    private App app;

    public void initialize(){
        for(PlayerColor color : PlayerColor.values())
            playerSets.add(PlayerSetController.getController(color));
        for(PlayerSetController controller : playerSets)
            playerSetsVBox.getChildren().add(controller.getRoot());
    }

    @Override
    public GridPane getRoot() {
        return gameBoard;
    }

    public  static ActionHandlerGUI getController(){
        return GUIView.getController("/view/ActionHandler/General/actionHandler.fxml", "/view/ActionHandler/General/actionHandler.css");
    }

    public void start(App app){
       this.actionHandlerScene = new Scene(this.getRoot(), 700, 300);
       this.app = app;
       app.setScene(this.actionHandlerScene);
    }

    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
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
