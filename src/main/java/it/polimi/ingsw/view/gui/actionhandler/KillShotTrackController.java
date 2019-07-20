package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.gui.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class KillShotTrackController implements Ifxml<StackPane>
{
    @FXML private VBox trackHBox;
    @FXML private StackPane rootPane;
    @FXML private ImageView firedSkull;
    @FXML VBox tearsHBox;
    private MatchSnapshot old;
    private static final int TOT_OTHER_SKULLS = 7;
    private List<ImageView> otherSkulls = new ArrayList<>();

    public void initialize() {
        firedSkull = createFiredSKull();
        for(int i = 0; i < TOT_OTHER_SKULLS; i++)
            otherSkulls.add(newSkull());
        for(ImageView skull : otherSkulls)
            trackHBox.getChildren().add(skull);
        trackHBox.getChildren().add(firedSkull);
        for(ImageView img : otherSkulls)
            img.setVisible(false);
        firedSkull.setVisible(false);
    }

    private void addTear(String color){
        ImageView tear = new ImageView(new Image("player/" + color.toLowerCase() + "_drop.png"));
        tear.fitHeightProperty().bind(trackHBox.heightProperty().multiply(0.4));
        tear.setPreserveRatio(true);
        tearsHBox.getChildren().add(tear);
    }

    private void buildController(MatchSnapshotProvider provider)
    {
        provider.modelChangedEvent().addEventHandler((a,snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot snapshot)
    {
        int totOld = old == null ? 0 : getSkullsCount();
        setupTears(snapshot, totOld);
        old = snapshot;
    }

    private int getSkullsCount(){
        return (int)otherSkulls.stream().filter(Node::isVisible).count() + (firedSkull.isVisible() ? 1 : 0);
    }

    private void setupTears(MatchSnapshot snapshot, int totOld) {
        List<List<String>> killShotTrack = snapshot.gameBoardSnapshot.getKillShotTrack();
        for (int i = totOld; i < killShotTrack.size(); i++)
            if (killShotTrack.get(i) == null)
                if (i < TOT_OTHER_SKULLS)
                    addSkull(otherSkulls.get(i), null);
                else
                    addSkull(firedSkull, null);
            else if (!killShotTrack.get(i).isEmpty())
            {
                int i2 = i;
                EventHandler<MouseEvent> mouseEventEventHandler = e -> onMouseOver(killShotTrack.get(i2));
                if (i < TOT_OTHER_SKULLS)
                    addSkull(otherSkulls.get(i), mouseEventEventHandler);
                else
                    addSkull(firedSkull, mouseEventEventHandler);
             }
    }

    private void onMouseOver(List<String> colors){
        tearsHBox.getChildren().clear();
        for(String color : colors)
            addTear(color);
        tearsHBox.setVisible(true);
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static KillShotTrackController getController(MatchSnapshotProvider provider){
        KillShotTrackController ret = GUIView.getController("/view/ActionHandler/killShotTrack/killShotTrack.fxml", "/view/ActionHandler/killShotTrack/killShotTrack.css");
        ret.buildController(provider);
        return ret;
    }

    private void addSkull(ImageView skull, EventHandler<? super MouseEvent> eventHandler){
        skull.setVisible(true);
        skull.setOnMouseEntered(eventHandler);
        skull.setOnMouseExited(e -> tearsHBox.setVisible(false));
        Animations.appearAnimation(skull);
    }

    private ImageView createFiredSKull(){
        ImageView ret = new ImageView(Cache.getImage("/skull_fired.png"));
        ret.fitHeightProperty().bind(rootPane.maxHeightProperty());
        ret.setPreserveRatio(true);
        ret.getStyleClass().add("button");
        return ret;
    }

    private ImageView newSkull(){
        ImageView ret = new ImageView(Cache.getImage("/skull.png"));
        ret.fitWidthProperty().bind(firedSkull.fitHeightProperty().divide(2.5));
        ret.setPreserveRatio(true);
        ret.getStyleClass().add("button");
        return ret;
    }
}
