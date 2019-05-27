package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.view.Login;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;


public class ColorChoiceController implements Ifxml<HBox> {
    @FXML private HBox hBox;

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public Circle newColorButton(String color){
        Circle ret = new Circle(30);
        ret.getStyleClass().add("button");
        ret.getStyleClass().add("colorButton");
        ret.setStyle("-fx-fill: " + color);
        hBox.getChildren().add(ret);
        return ret;
    }

    public static ColorChoiceController getController() {
        return GUIView.getController("/view/login/colorChoiceHBox/colorChoice.fxml", "/view/login/colorChoiceHBox/colorChoice.css");
    }
}
