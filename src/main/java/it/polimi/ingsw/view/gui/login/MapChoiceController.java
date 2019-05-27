package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;

public class MapChoiceController implements Ifxml<HBox>
{
    public static final int TOT_MAPS = 4;
    @FXML private HBox hBox;
    @FXML private HBox imageBox;
    private ImageView[] maps = new ImageView[TOT_MAPS];
    private Polygon nextButton;

    public void C()
    {
        for(int i=0; i < maps.length; i++)
            maps[i] = newMap(i);
        imageBox.getChildren().add(maps[0]);
        hBox.getChildren().add(nextMapButton());
    }

    private Polygon nextMapButton(){
        final double scale = 0.5;
        Polygon triangle = new Polygon(scale*80, scale*40, 0, scale*80, 0, 0);
        triangle.getStyleClass().add("button");
        triangle.getStyleClass().add("arrowButton");
        IntegerProperty i = new SimpleIntegerProperty(0);
        triangle.setOnMouseClicked(e -> {
            i.set(i.get() == TOT_MAPS - 1 ? 0 : i.get() + 1);
            imageBox.getChildren().clear();
            imageBox.getChildren().add(maps[i.get()]);
        });
        return nextButton = triangle;
    }

    public ImageView[] getMaps() {return maps; }

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public Polygon getNextButton(){
        return nextButton;
    }

    private ImageView newMap(int i)
    {
        final String partialUrl = "map";
        String url = partialUrl + i +".png";
        ImageView ret = new ImageView(new Image(url));
        ret.getStyleClass().add("mapButton");
        ret.setFitWidth(700);
        ret.setFitHeight(536);
        return ret;
    }

    public static MapChoiceController getController() {
        return GUIView.getController("/view/login/mapChoiceHBox/MapChoice.fxml", "/view/login/mapChoiceHBox/MapChoice.css");
    }
}
