package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class ShopController implements Ifxml<StackPane>
{
    @FXML private StackPane rootPane;

    @FXML private ImageView weapon1;
    @FXML private Button buyButton1;

    @FXML private ImageView weapon2;
    @FXML private Button buyButton2;

    @FXML private ImageView weapon3;
    @FXML private Button buyButton3;


    public List<ImageView> getWeapons(){
        return Arrays.asList(weapon1, weapon2, weapon3);
    }

    public void initialize(){
        for(ImageView weapon : getWeapons())
            weapon.setImage(new Image("weapon/hellion.png"));
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static ShopController getController(){
        return GUIView.getController("/view/ActionHandler/shop/shop.fxml", "/view/ActionHandler/shop/shop.css");
    }
}
