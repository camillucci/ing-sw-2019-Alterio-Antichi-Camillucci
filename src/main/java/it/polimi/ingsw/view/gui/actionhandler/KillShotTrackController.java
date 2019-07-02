package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.view.gui.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class KillShotTrackController implements Ifxml<StackPane>
{
    @FXML private HBox trackHBox;
    @FXML private StackPane rootPane;
    @FXML private ImageView firedSkull;
    private MatchSnapshotProvider provider;
    private MatchSnapshot old;
    private static final int TOT_OTHER_SKULLS = 7;
    @FXML private List<ImageView> otherSkulls = new ArrayList<>();

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

    private void buildController(MatchSnapshotProvider provider)
    {
        this.provider = provider;
        provider.modelChangedEvent().addEventHandler((a,snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot snapshot)
    {
        int totOld = old == null ? 0 : old.gameBoardSnapshot.skulls;
        int totNew = snapshot.gameBoardSnapshot.skulls;
        old = provider.getMatchSnapshot();

        int i;
        for(i = totOld; i < Math.min(totNew, TOT_OTHER_SKULLS); i++)
        {
            otherSkulls.get(i).setVisible(true);
            Animations.appearAnimation(otherSkulls.get(i));
        }
        if(i == TOT_OTHER_SKULLS)
        {
            firedSkull.setVisible(true);
            Animations.appearAnimation(firedSkull);
        }
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

    private ImageView createFiredSKull(){
        ImageView ret = new ImageView(Cache.getImage(getClass().getResourceAsStream("/skull_fired.png")));
        ret.fitHeightProperty().bind(rootPane.maxHeightProperty());
        ret.setPreserveRatio(true);
        return ret;
    }

    private ImageView newSkull(){
        ImageView ret = new ImageView(Cache.getImage(getClass().getResourceAsStream("/skull.png")));
        ret.fitWidthProperty().bind(firedSkull.fitHeightProperty().divide(2.5));
        ret.setPreserveRatio(true);
        return ret;
    }
}
