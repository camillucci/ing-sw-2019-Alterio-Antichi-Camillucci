package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

public class MapChoiceController implements Ifxml<HBox>
{
    public static final int TOT_MAPS = 4;
    @FXML private HBox hBox;
    @FXML private VBox leftImageBox;
    @FXML private VBox imageBox;
    @FXML private VBox rightImageBox;
    private IntegerProperty mapIndex = new SimpleIntegerProperty(0);
    private ImageView[] maps = new ImageView[TOT_MAPS];
    private Polygon rightNextButton;
    private Polygon leftNextButton;

    public void initialize()
    {
        for(int i=0; i < maps.length; i++)
            maps[i] = newMap(i);
        leftImageBox.getChildren().add(leftNextMapButton());
        imageBox.getChildren().add(maps[0]);
        rightImageBox.getChildren().add(rightNextMapButton());
    }

    private Polygon leftNextMapButton(){
        final double scale = 0.5;
        Polygon triangle = new Polygon(0, scale*40, scale*80, scale*80, scale*80, 0);
        triangle.getStyleClass().add("button");
        triangle.getStyleClass().add("arrowButton");
        triangle.setOnMouseClicked(e -> {
            mapIndex.set(mapIndex.get() == 0 ? TOT_MAPS - 1 : mapIndex.get() - 1);
            imageBox.getChildren().clear();
            imageBox.getChildren().add(maps[mapIndex.get()]);
        });
        leftNextButton = triangle;
        return triangle;
    }

    private Polygon rightNextMapButton(){
        final double scale = 0.5;
        Polygon triangle = new Polygon(scale*80, scale*40, 0, scale*80, 0, 0);
        triangle.getStyleClass().add("button");
        triangle.getStyleClass().add("arrowButton");
        triangle.setOnMouseClicked(e -> {
            mapIndex.set(mapIndex.get() == TOT_MAPS - 1 ? 0 : mapIndex.get() + 1);
            imageBox.getChildren().clear();
            imageBox.getChildren().add(maps[mapIndex.get()]);
        });
        rightNextButton = triangle;
        return triangle;
    }

    public ImageView[] getMaps() {
        return maps;
    }

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public Polygon getLeftNextButton(){
        return leftNextButton;
    }

    public Polygon getRightNextButton(){
        return rightNextButton;
    }

    private ImageView newMap(int i)
    {
        final String partialUrl = "/map";
        String url = partialUrl + i +".png";
        ImageView ret = new ImageView(new Image(getClass().getResourceAsStream(url)));
        ret.getStyleClass().add("mapButton");
        ret.setFitWidth(700);
        ret.setFitHeight(536);
        return ret;
    }

    public static MapChoiceController getController() {
        return GUIView.getController("/view/login/mapChoiceHBox/MapChoice.fxml", "/view/login/mapChoiceHBox/MapChoice.css");
    }
}
