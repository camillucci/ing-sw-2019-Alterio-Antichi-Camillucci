package it.polimi.ingsw.view.gui.login;

import it.polimi.ingsw.view.gui.Cache;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class SkullChoiceController implements Ifxml<HBox>
{
    @FXML private HBox hBox;
    @FXML private Rectangle rect1;
    @FXML private Rectangle rect2;
    @FXML private Rectangle rect3;
    @FXML private Rectangle rect4;
    @FXML private Rectangle rect5;
    @FXML private Rectangle rect6;
    @FXML private Rectangle rect7;
    @FXML private Rectangle rect8;

    public void initialize(){
        Rectangle[] skulls = getButtons();
        for(Rectangle rectangle : skulls)
            rectangle.setFill(new ImagePattern(Cache.getImage(getClass().getResourceAsStream("/skull.png"))));
        for(int i = 4; i < skulls.length; i++){
            final int i2 = i;
            skulls[i].setOnMouseEntered(e -> {
                    for(int j=0; j <= i2; j++)
                        skulls[j].getStyleClass().add("buttonSkullHover");
            });
            skulls[i].setOnMouseExited(e -> {
                    for(int j=0; j <= i2; j++)
                        skulls[j].getStyleClass().remove("buttonSkullHover");
            });
        }
    }

    public Rectangle[] getButtons() {
        return new Rectangle[] {rect1, rect2, rect3, rect4, rect5, rect6, rect7, rect8};
    }

    @Override
    public HBox getRoot() {
        return hBox;
    }

    public static SkullChoiceController getController(){
       return GUIView.getController("/view/login/skullChoiceHBox/SkullChoice.fxml", "/view/login/skullChoiceHBox/SkullChoice.css");
    }
}
