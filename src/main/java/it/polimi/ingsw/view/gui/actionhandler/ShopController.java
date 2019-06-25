package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.view.gui.GUIView;

public class ShopController extends SelectionBoxController
{
    public static ShopController getController(String ... paths){
        return GUIView.getController("/view/ActionHandler/selectionBox/selectionBox.fxml", "/view/ActionHandler/selectionBox/selectionBox.css");
    }
}
