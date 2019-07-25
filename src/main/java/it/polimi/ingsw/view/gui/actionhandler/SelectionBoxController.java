package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.gui.Cache;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionBoxController implements Ifxml<StackPane>
{
    @FXML Text title;
    @FXML private HBox selectionHBox;
    @FXML private StackPane rootPane;
    private List<ImageView> options = new ArrayList<>();
    private static final double SPACING_FACTOR = 0.1;


    protected void buildController(List<RemoteAction> actions)
    {
        buildController(
                actions.stream().map(a -> a.visualizable.imgPath).collect(Collectors.toList()),
                actions.stream().map(a -> a.visualizable.description).collect(Collectors.toList()),
                actions.get(0).visualizable.name);
    }

    protected void buildController(List<String> paths, List<String> descriptions, String title)
    {
        selectionHBox.spacingProperty().bind(rootPane.minWidthProperty().multiply(SPACING_FACTOR));
        for(int i=0; i < paths.size(); i++){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            ImageView img = new ImageView(Cache.getImage(paths.get(i).toLowerCase()));

            img.fitWidthProperty().bind(selectionHBox.minWidthProperty().divide(Math.max(paths.size(), 3)));
            img.setPreserveRatio(true);
            img.getStyleClass().add("button");
            options.add(img);
            vBox.spacingProperty().bind(selectionHBox.spacingProperty());
            vBox.getChildren().add(img);
            Label text = createText(descriptions.get(i));
            text.minWidthProperty().bind(img.fitWidthProperty().multiply(1.5));
            text.maxWidthProperty().bind(img.fitWidthProperty().multiply(1.5));
            vBox.getChildren().add(text);
            selectionHBox.getChildren().add(vBox);
        }
        this.title.setText(title);
    }

    private Label createText(String text){
        Label ret = new Label(text);
        ret.setTextAlignment(TextAlignment.CENTER);
        ret.setWrapText(true);
        ret.setFocusTraversable(false);
        ret.getStyleClass().add("text-area");
        return ret;
    }

    public List<ImageView> getOptions(){
        return new ArrayList<>(options);
    }

    private static SelectionBoxController getController()
    {
        return GUIView.getController("/view/ActionHandler/selectionBox/selectionBox.fxml", "/view/ActionHandler/selectionBox/selectionBox.css");
    }

    @Override
    public StackPane getRoot() {
        return rootPane;
    }

    public static SelectionBoxController getController(List<RemoteAction> actions){
        SelectionBoxController ret = getController();
        ret.buildController(actions);
        return ret;
    }

    public static SelectionBoxController getController(List<String> paths, List<String> descriptions, String title){
        SelectionBoxController ret = getController();
        ret.buildController(paths, descriptions, title);
        return ret;
    }
}
