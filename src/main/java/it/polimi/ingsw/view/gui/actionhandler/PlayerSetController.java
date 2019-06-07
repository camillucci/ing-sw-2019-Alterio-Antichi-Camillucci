package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class PlayerSetController implements Ifxml<VBox> {
    @FXML private GridPane rec;
    @FXML private VBox vBox;
    @FXML private Pane tear0;
    @FXML private Pane tear1;
    @FXML private Pane tear2;
    @FXML private Pane tear3;
    @FXML private Pane tear4;
    @FXML private Pane tear5;
    @FXML private Pane tear6;
    @FXML private Pane tear7;
    @FXML private Pane tear8;
    @FXML private Pane tear9;
    @FXML private Pane tear10;
    @FXML private Pane tear11;
    private List<Pane> tears;
    private PlayerColor color;
    private int totDamage = 0;

    public void initialize()
    {
        tears = getTears();
        for(Pane p : tears)
            addDamage(PlayerColor.BLUE);
    }

    @Override
    public VBox getRoot() {
        return vBox;
    }

    private List<Pane> getTears(){
        return Arrays.asList(tear0, tear1, tear2, tear3, tear4, tear5, tear6, tear7, tear8, tear9, tear10, tear11);
    }

    private void setColor(PlayerColor color){
        this.color = color;
        vBox.setStyle("-fx-background-image: url(\"player/" + color.getName() + "1.png\")");
    }

    void addDamage(PlayerColor color){
        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(tears.get(totDamage).widthProperty().multiply(0.7));
        imageView.fitHeightProperty().bind(tears.get(totDamage).heightProperty());
        imageView.setImage(new Image("player/" + color.getName() + "_drop.png"));
        tears.get(totDamage++).getChildren().add(imageView);
    }

    public static PlayerSetController getController(PlayerColor color){
        PlayerSetController ret = GUIView.getController("/view/ActionHandler/actionHandler/playerSet.fxml", "/view/ActionHandler/actionHandler/playerSet.css");
        ret.setColor(color);
        return ret;
    }
}
