package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
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
    private static final int TOT_OTHER_SKULLS = 7;
    @FXML private List<ImageView> otherSkulls = new ArrayList<>();

    public void initialize() {
        firedSkull = createFiredSKull();
        for(int i = 0; i < TOT_OTHER_SKULLS; i++)
            otherSkulls.add(newSkull());
        for(ImageView skull : otherSkulls)
            trackHBox.getChildren().add(skull);
        trackHBox.getChildren().add(firedSkull);
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static KillShotTrackController getController(){
        return GUIView.getController("/view/ActionHandler/killShotTrack/killShotTrack.fxml", "/view/ActionHandler/killShotTrack/killShotTrack.css");
    }

    private ImageView createFiredSKull(){
        ImageView ret = new ImageView(new Image("skull_fired.png"));
        ret.fitHeightProperty().bind(rootPane.maxHeightProperty());
        ret.setPreserveRatio(true);
        return ret;
    }

    private ImageView newSkull(){
        ImageView ret = new ImageView(new Image("skull.png"));
        ret.fitWidthProperty().bind(firedSkull.fitHeightProperty().divide(2.5));
        ret.setPreserveRatio(true);
        return ret;
    }
}
