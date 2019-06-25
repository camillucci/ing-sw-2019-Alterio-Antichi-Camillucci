package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionBoxController implements Ifxml<StackPane>
{
    @FXML private Text descriptionText;
    @FXML private HBox selectionHBox;
    @FXML private StackPane rootPane;
    private List<ImageView> options = new ArrayList<>();
    private static final double SPACING_FACTOR = 0.1;


    protected void buildController(List<RemoteAction> actions)
    {
        selectionHBox.spacingProperty().bind(rootPane.minWidthProperty().multiply(SPACING_FACTOR));
        for(RemoteAction action : actions){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            ImageView img = new ImageView(action.visualizable.imgPath.toLowerCase());
            img.fitWidthProperty().bind(selectionHBox.minWidthProperty().divide(actions.size()));
            img.setPreserveRatio(true);
            img.getStyleClass().add("button");
            options.add(img);
            vBox.spacingProperty().bind(selectionHBox.spacingProperty());
            vBox.getChildren().add(img);
            vBox.getChildren().add(createText(action.visualizable.description));
            selectionHBox.getChildren().add(vBox);
        }
    }

    private Text createText(String text){
        Text ret = new Text(text);
        ret.getStyleClass().add("description");
        return ret;
    }

    public List<ImageView> getOptions(){
        return new ArrayList<>(options);
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static SelectionBoxController getController(List<RemoteAction> actions){
        SelectionBoxController ret = GUIView.getController("/view/ActionHandler/selectionBox/selectionBox.fxml", "/view/ActionHandler/selectionBox/selectionBox.css");
        ret.buildController(actions);
        return ret;
    }
}
