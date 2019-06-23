package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

public class SquareController implements Ifxml<StackPane> {
    @FXML private ImageView ammoCard_shop;
    @FXML private StackPane squareRootPane;
    @FXML private ImageView avatar1;
    @FXML private ImageView avatar2;
    @FXML private ImageView avatar3;
    @FXML private ImageView avatar4;
    @FXML private ImageView avatar5;


    private void createAvatars()
    {
        for (ImageView avatar : getAvatars())
            avatar.setImage(new Image("player/violet_avatar.png"));
    }

    private void buildSquare()
    {
        createAvatars();
        ammoCard_shop.setImage(new Image("ammo/1r_1y_pu.png"));
    }

    private void buildSquare(AmmoColor shopColor){
        createAvatars();
        ammoCard_shop.setImage(new Image("shop_" + shopColor.getName() + ".png"));
    }

    public List<ImageView> getAvatars(){
        return Arrays.asList(avatar1, avatar2, avatar3, avatar4, avatar5);
    }

    @Override
    public StackPane getRoot() {
        return squareRootPane;
    }

    private static SquareController createController(){
        return GUIView.getController("/view/ActionHandler/map/square/square.fxml", "/view/ActionHandler/map/square/square.css");
    }

    public static SquareController getController(AmmoColor shopColor){
        SquareController ret = createController();
        ret.buildSquare(shopColor);
        return ret;
    }

    public static SquareController getController(){
        SquareController ret = createController();
        ret.buildSquare();
        return ret;
    }

}
