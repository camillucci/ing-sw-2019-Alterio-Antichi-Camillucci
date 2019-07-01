package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;


public class ColorChoiceController implements Ifxml<HBox> {
    @FXML private HBox hBox;

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public ImageView newColorButton(String color){
        //Circle ret = new Circle(30);
        final String partialUrl = "_avatar";
        String url = "/player/" + color.toLowerCase() + partialUrl + ".png";
        ImageView ret = new ImageView(new Image(getClass().getResourceAsStream(url)));
        ret.getStyleClass().add("button");
        ret.getStyleClass().add("colorButton");
        hBox.getChildren().add(ret);
        return ret;
    }

    public static ColorChoiceController getController() {
        return GUIView.getController("/view/login/colorChoiceHBox/colorChoice.fxml", "/view/login/colorChoiceHBox/colorChoice.css");
    }
}
