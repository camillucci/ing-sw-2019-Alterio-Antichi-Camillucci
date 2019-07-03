package it.polimi.ingsw.view.gui.endGame;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import it.polimi.ingsw.view.gui.RemoteActionsProvider;
import it.polimi.ingsw.view.gui.actionhandler.AmmoBoxController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class EndGameController implements Ifxml<Pane> {
    @FXML private Pane rootPane;
    private String[][] scoreBoard;

    @Override
    public Pane getRoot() {
        return rootPane;
    }

    private void buildController(String[][] scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public static EndGameController getController(String[][] scoreBoard){
        EndGameController ret = GUIView.getController("/view/endGame/endGame.fxml", "/view/endGame/endGame.css");
        ret.buildController(scoreBoard);
        return ret;
    }
}
