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

    public HBox[] getRows(){
        return new HBox[]{row1, row2, row3, row4, row5};
    }

    public synchronized void newPlayerJoined(String name){
        HBox[] rows = getRows();

        for(int i=0; i < rows.length; i++){
            Label label = (Label)rows[i].getChildren().get(1);
            if(label.getText().equals("")){
                Rectangle rectangle = (Rectangle)rows[i].getChildren().get(0);
                rectangle.getStyleClass().add("joinedRectangle");
                label.setText(name);
                return;
            }
        }
    }

    public synchronized void playerLeft(String name){
        HBox[] rows = getRows();

        for(int i=0; i < rows.length; i++){
            Label label = (Label)rows[i].getChildren().get(1);
            if(label.getText().equals(name)){
                Rectangle rectangle = (Rectangle)rows[i].getChildren().get(0);
                rectangle.getStyleClass().remove("joinedRectangle");
                label.setText("");
                return;
            }
        }
    }

    @Override
    public VBox getRoot() {
        return vBox;
    }

    public static RoomJoinController getController(){
        return GUIView.getController("/view/login/roomJoinVBox/RoomJoin.fxml", "/view/login/roomJoinVBox/RoomJoin.css");
    }
}
