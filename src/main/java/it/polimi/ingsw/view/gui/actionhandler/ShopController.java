package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.snapshots.SquareSnapshot;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopController
{
    private StackPane root = new StackPane();
    private String color;

    private ShopController(){}

    public StackPane getRoot()
    {
        return root;
    }

    private void buildController(String color, MatchSnapshotProvider provider)
    {
        this.color = color;
        provider.modelChangedEvent().addEventHandler((a, snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot snapshot)
    {
        List<String> descriptions = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        SquareSnapshot[][] squares = snapshot.gameBoardSnapshot.squareSnapshots;

        for (SquareSnapshot[] square : squares)
            for (SquareSnapshot squareSnapshot : square)
                if (isRightShop(squareSnapshot, this.color)) {
                    descriptions.addAll(squareSnapshot.getCards());
                    paths.addAll(squareSnapshot.getCards().stream().map(this::nameToPath).collect(Collectors.toList()));
                }

        root.getChildren().clear();
        SelectionBoxController controller = SelectionBoxController.getController(paths, descriptions, "welcome to the " + color + " shop");
        controller.getRoot().minHeightProperty().bind(root.minHeightProperty());
        controller.getRoot().maxHeightProperty().bind(root.maxHeightProperty());
        controller.getRoot().minWidthProperty().bind(root.minWidthProperty());
        controller.getRoot().maxWidthProperty().bind(root.maxWidthProperty());
        root.getChildren().add(controller.getRoot());
    }

    private boolean isRightShop(SquareSnapshot square, String color)
    {
        return square != null && square.name.toLowerCase().contains(color.toLowerCase());
    }

    private String nameToPath(String name)
    {
        return "/weapon/" + name.replace(" ", "_").concat(".png").toLowerCase();
    }

    public static ShopController getController(MatchSnapshotProvider provider, String color)
    {
        ShopController ret = new ShopController();
        ret.buildController(color, provider);
        return ret;
    }
}
