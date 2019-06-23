package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    @FXML private ImageView weapon2;
    @FXML private ImageView weapon3;
    private HBox hBox;
    private VBox vBox;
    private static final double spacingFactor = 0.04;

    public List<ImageView> getWeapons(){
        return Arrays.asList(weapon1, weapon2, weapon3);
    }

    public void initialize(){
        for(ImageView weapon : getWeapons())
            weapon.setImage(new Image("weapon/lock_rifle.png"));
    }

    private void buildShop(boolean horizontal){
        if(horizontal)
            buildHorizontal();
        else
            buildVertical();
    }

    private void buildHorizontal(){
        hBox = new HBox();
        hBox.minHeightProperty().bind(rootPane.minHeightProperty());
        hBox.maxHeightProperty().bind(rootPane.maxHeightProperty());
        hBox.spacingProperty().bind(rootPane.maxWidthProperty().multiply(spacingFactor));
        for(ImageView weapon : getWeapons()){
            weapon.fitHeightProperty().bind(hBox.maxHeightProperty());
            weapon.setPreserveRatio(true);
            hBox.getChildren().add(weapon);
        }
        rootPane.getChildren().add(hBox);
    }

    private void buildVertical(){
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.minHeightProperty().bind(rootPane.minHeightProperty());
        vBox.maxHeightProperty().bind(rootPane.maxHeightProperty());
        vBox.spacingProperty().bind(rootPane.maxHeightProperty().multiply(spacingFactor));
        for(ImageView weapon : getWeapons()){
            weapon.fitHeightProperty().bind(vBox.maxHeightProperty().subtract(vBox.spacingProperty().multiply(2)).divide(3));
            weapon.setPreserveRatio(true);
            vBox.getChildren().add(weapon);
        }
        rootPane.getChildren().add(vBox);
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static ShopController getController(boolean horizontal){
        ShopController ret = GUIView.getController("/view/ActionHandler/shop/shop.fxml", "/view/ActionHandler/shop/shop.css");
        ret.buildShop(horizontal);
        return ret;
    }
}
