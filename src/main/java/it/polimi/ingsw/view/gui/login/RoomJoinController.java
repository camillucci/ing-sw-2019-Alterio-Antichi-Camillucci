package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;


public class RoomJoinController implements Ifxml<VBox>
{
    @FXML private VBox vBox;
    @FXML private HBox row1;
    @FXML private HBox row2;
    @FXML private HBox row3;
    @FXML private HBox row4;
    @FXML private HBox row5;
    private int totJoined = 0;

    public HBox[] getRows(){
        return new HBox[]{row1, row2, row3, row4, row5};
    }

    public void newPlayerJoined(String name){
        Rectangle rectangle = (Rectangle) getRows()[totJoined].getChildren().get(0);
        rectangle.getStyleClass().add("joinedRectangle");
        Label playerLabel = (Label) getRows()[totJoined++].getChildren().get(1);
        playerLabel.setText(name);
    }

    @Override
    public VBox getRoot() {
        return vBox;
    }

    public static RoomJoinController getController(){
        return GUIView.getController("/view/login/roomJoinVBox/RoomJoin.fxml", "/view/login/roomJoinVBox/RoomJoin.css");
    }
}
