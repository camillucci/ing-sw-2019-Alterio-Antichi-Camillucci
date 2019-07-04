package it.polimi.ingsw.view.gui.actionhandler;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.gui.*;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.Arrays;
import java.util.List;

public class AmmoBoxController implements Ifxml<VBox>
{
    public final IEvent<AmmoBoxController, String> addDiscardedAmmoEvent = new Event<>();
    @FXML private AmmoImageView yellowAmmo;
    @FXML private AmmoImageView redAmmo;
    @FXML private AmmoImageView blueAmmo;
    @FXML private Text redText;
    @FXML private Text blueText;
    @FXML private Text yellowText;
    @FXML private VBox rootPane;
    private List<AmmoImageView> ammos;
    private String color;
    private MatchSnapshotProvider provider;
    private RemoteActionsProvider actionProvider;

    public void initialize(){
        redText.setText("0");
        blueText.setText("0");
        yellowText.setText("0");
        ammos = getAmmos();
    }

    private List<AmmoImageView> getAmmos(){
        return Arrays.asList(yellowAmmo, redAmmo, blueAmmo);
    }

    @Override
    public VBox getRoot() {
        return rootPane;
    }

    private void buildController(String color, MatchSnapshotProvider provider, RemoteActionsProvider actionsProvider)
    {
        this.color = color;
        this.provider = provider;
        this.actionProvider = actionsProvider;
        provider.modelChangedEvent().addEventHandler((a, snapshot) -> onModelChanged(snapshot));
        if(actionsProvider != null)
            actionsProvider.newActionsEvent().addEventHandler((a, action) -> onNewAction(action));
    }

    private void onNewAction(RemoteAction action)
    {
        RemoteAction.Data data = action.getData();
        if(data.getDiscardableAmmos().isEmpty())
            return;
        for(String color : data.getDiscardableAmmos())
            for(AmmoImageView ammo : ammos)
                if(ammo.getColor().equalsIgnoreCase(color))
                {
                    ammo.getStyleClass().add("button");
                    ammo.setOnMouseClicked(e -> disableAnd(() -> invoke(addDiscardedAmmoEvent, color)));
                }
    }

    private void disableAnd(Runnable func)
    {
        for(AmmoImageView ammo : ammos)
        {
            ammo.setOnMouseClicked(null);
            ammo.getStyleClass().remove("button");
        }
        func.run();
    }

    private <T> void invoke(IEvent<AmmoBoxController, T> event, T arg)
    {
        ((Event<AmmoBoxController, T>)event).invoke(this, arg);
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

    public static AmmoBoxController getController(String color, MatchSnapshotProvider provider)
    {
        return getController(color,provider, null);
    }

    public static AmmoBoxController getController(String color, MatchSnapshotProvider provider, RemoteActionsProvider actionsProvider){
        AmmoBoxController ret = GUIView.getController("/view/ActionHandler/ammoBox/ammoBox.fxml", "/view/ActionHandler/ammoBox/ammoBox.css");
        ret.buildController(color, provider, actionsProvider);
        return ret;
    }
}

