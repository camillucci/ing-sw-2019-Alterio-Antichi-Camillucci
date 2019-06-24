package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
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
    private MatchSnapshotProvider matchSnapshotProvider;

    public void initialize()
    {
        tears = getTears();
    }

    @Override
    public VBox getRoot() {
        return vBox;
    }

    private List<Pane> getTears(){
        return Arrays.asList(tear0, tear1, tear2, tear3, tear4, tear5, tear6, tear7, tear8, tear9, tear10, tear11);
    }

    private void build(PlayerColor color, MatchSnapshotProvider provider){
        this.color = color;
        this.matchSnapshotProvider = provider;
        matchSnapshotProvider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
        vBox.setStyle("-fx-background-image: url(\"player/" + color.getName() + "1.png\")");
    }

    private void onModelChanged(MatchSnapshot matchSnapshot){
        clear();
        PublicPlayerSnapshot player = null;
        if(matchSnapshot.privatePlayerSnapshot.color.equals(this.color.getName()))
            player = matchSnapshot.privatePlayerSnapshot;
        else
            for(PublicPlayerSnapshot p : matchSnapshot.getPublicPlayerSnapshot())
                if(p.color.equals(this.color.getName()))
                {
                    player = p;
                    break;
                }
        for(String damage : player.getDamage())
            addDamage(damage);
    }

    private void clear(){
        for(Pane tear : tears)
            tear.getChildren().clear();

    }

    void addDamage(String color){
        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(tears.get(totDamage).widthProperty().multiply(0.7));
        imageView.fitHeightProperty().bind(tears.get(totDamage).heightProperty());
        imageView.setImage(new Image("player/" + color + "_drop.png"));
        tears.get(totDamage++).getChildren().add(imageView);
    }

    public static PlayerSetController getController(PlayerColor color, MatchSnapshotProvider matchSnapshotProvider){
        PlayerSetController ret = GUIView.getController("/view/ActionHandler/actionHandler/playerSet.fxml", "/view/ActionHandler/actionHandler/playerSet.css");
        ret.build(color, matchSnapshotProvider);
        return ret;
    }
}
