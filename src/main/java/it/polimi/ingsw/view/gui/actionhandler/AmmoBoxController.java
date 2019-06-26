package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.Animations;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AmmoBoxController implements Ifxml<VBox>
{
    @FXML private ImageView yellowAmmo;
    @FXML private ImageView redAmmo;
    @FXML private ImageView blueAmmo;
    @FXML private Text redText;
    @FXML private Text blueText;
    @FXML private Text yellowText;
    @FXML private VBox rootPane;
    private String color;
    private MatchSnapshotProvider provider;


    public void initialize(){
        redText.setText("0");
        blueText.setText("0");
        yellowText.setText("0");
    }

    @Override
    public VBox getRoot() {
        return rootPane;
    }

    private void buildController(String color, MatchSnapshotProvider provider)
    {
        this.color = color;
        this.provider = provider;
        provider.modelChangedEvent().addEventHandler((a, snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot snapshot) {
        PublicPlayerSnapshot player = null;
        if(snapshot.privatePlayerSnapshot.color.equals(this.color))
            player = snapshot.privatePlayerSnapshot;
        else
            for(PublicPlayerSnapshot p : snapshot.getPublicPlayerSnapshot())
                if(p.color.equals(this.color))
                {
                    player = p;
                    break;
                }

        assert player != null;

        int red = Integer.parseInt(redText.getText()), newRed = player.redAmmo;
        int blue = Integer.parseInt(blueText.getText()), newBlue = player.blueAmmo;
        int yellow = Integer.parseInt(yellowText.getText()), newYellow = player.yellowAmmo;

        if(newRed != red)
        {
            redText.setText(Integer.toString(newRed));
            Animations.appearAnimation(redText);
        }
        if(newBlue != blue)
        {
            blueText.setText(Integer.toString(newBlue));
            Animations.appearAnimation(blueText);
        }
        if(newYellow != yellow)
        {
            yellowText.setText(Integer.toString(newYellow));
            Animations.appearAnimation(yellowText);
        }
        blueText.setText(Integer.toString(player.blueAmmo));
        yellowText.setText(Integer.toString(player.yellowAmmo));
    }

    public static AmmoBoxController getController(String color, MatchSnapshotProvider provider){
        AmmoBoxController ret = GUIView.getController("/view/ActionHandler/ammoBox/ammoBox.fxml", "/view/ActionHandler/ammoBox/ammoBox.css");
        ret.buildController(color, provider);
        return ret;
    }

}
