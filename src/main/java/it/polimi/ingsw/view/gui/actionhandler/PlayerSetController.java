package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;

public class PlayerSetController implements Ifxml<StackPane> {
    @FXML private HBox skullsHBox;
    @FXML private HBox markHBox;
    @FXML private ImageView background;
    @FXML private StackPane pane;
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
    private String color;
    private int totDamage = 0;
    private MatchSnapshotProvider matchSnapshotProvider;
    private MatchSnapshot old;
    private static final String PLAYER_PATH = "/player/";

    public void initialize()
    {
        tears = getTears();
    }

    @Override
    public StackPane getRoot() {
        return pane;
    }

    private List<Pane> getTears(){
        return Arrays.asList(tear0, tear1, tear2, tear3, tear4, tear5, tear6, tear7, tear8, tear9, tear10, tear11);
    }

    private void onModelChanged(MatchSnapshot matchSnapshot){
        reset();
        PublicPlayerSnapshot player = null;
        if(matchSnapshot.privatePlayerSnapshot.color.equals(this.color))
            player = matchSnapshot.privatePlayerSnapshot;
        else
            for(PublicPlayerSnapshot p : matchSnapshot.getPublicPlayerSnapshot())
                if(p.color.equals(this.color))
                {
                    player = p;
                    break;
                }
        setBackground(player);
        setMarks(player);
        setSkulls(player);
        addDamages(player);
        old = matchSnapshot;
    }

    private void addDamages(PublicPlayerSnapshot player){
        for(String damage : player.getDamage())
            addDamage(damage);
    }

    private void setSkulls(PublicPlayerSnapshot player) {
        for(int i=0; i < player.skull; i++)
            addSkull();
    }

    private void addSkull(){
        skullsHBox.getChildren().add(getSkull());
    }

    private ImageView getSkull(){
        ImageView skull = new ImageView(new Image("skull.png"));
        skull.setPreserveRatio(true);
        skull.fitHeightProperty().bind(skullsHBox.minHeightProperty().multiply(0.7));
        return skull;
    }

    private void setMarks(PublicPlayerSnapshot player) {
        String cur = "";
        int tot = 0;
        for(String color : player.getMark())
        {
            if (cur.equals(color))
                tot++;
            else if(tot == 3)
            {
                addMark(getDoubledMark(color));
                addMark(getMark(color));
            }
            else
            {
                for (int i = 0; i < tot; i++)
                    addMark(getMark(color));
                tot = 1;
                cur = color;
            }
        }
    }

    private void addMark(ImageView mark)
    {
        mark.fitHeightProperty().bind(markHBox.minHeightProperty().multiply(0.5));
        markHBox.getChildren().add(mark);
    }

    private ImageView getMark(String color)
    {
        ImageView ret = new ImageView(new Image(getDropUrl(color)));
        ret.setPreserveRatio(true);
        return ret;
    }

    private ImageView getDoubledMark(String color)
    {
        ImageView ret = new ImageView(new Image(getDoubledDropUrl(color)));
        ret.setPreserveRatio(true);
        return ret;
    }

    private void setBackground(PublicPlayerSnapshot player){
        String url = PLAYER_PATH + color.toLowerCase() + player.finalFrenzy  + ".png";
        background.setImage(new Image(getClass().getResourceAsStream(url)));
    }

    private void reset(){
        for(Pane tear : tears)
            tear.getChildren().clear();
        totDamage = 0;
        markHBox.getChildren().clear();
        skullsHBox.getChildren().clear();
    }

    private String getDropUrl(String color)
    {
        return PLAYER_PATH + color.toLowerCase() + "_drop.png";
    }

    private String getDoubledDropUrl(String color)
    {
        return PLAYER_PATH + color.toLowerCase() + "_drop2.png";
    }


    private void addDamage(String color){
        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(tears.get(totDamage).widthProperty().multiply(0.7));
        imageView.fitHeightProperty().bind(tears.get(totDamage).heightProperty());
        String url = getDropUrl(color);
        imageView.setImage(new Image(getClass().getResourceAsStream(url)));
        tears.get(totDamage++).getChildren().add(imageView);
    }

    private void build(String color, MatchSnapshotProvider provider){
        this.color = color;
        this.matchSnapshotProvider = provider;
        matchSnapshotProvider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
    }

    public static PlayerSetController getController(String color, MatchSnapshotProvider matchSnapshotProvider){
        PlayerSetController ret = GUIView.getController("/view/ActionHandler/actionHandler/playerSet.fxml", "/view/ActionHandler/actionHandler/playerSet.css");
        ret.build(color, matchSnapshotProvider);
        return ret;
    }
}
