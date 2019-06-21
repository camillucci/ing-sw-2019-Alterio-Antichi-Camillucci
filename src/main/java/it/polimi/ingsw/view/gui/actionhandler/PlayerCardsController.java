package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.List;

public class PlayerCardsController implements Ifxml<HBox> {

    @FXML private ImageView weapon1;
    @FXML private ImageView weapon2;
    @FXML private ImageView weapon3;
    @FXML private ImageView powerup1;
    @FXML private ImageView powerup2;
    @FXML private ImageView powerup3;
    @FXML private HBox cardsHBox;
    private int totWeapons = 0, totPowerups = 0;

    public void initialize()
    {
        addWeapon("furnace");
        addWeapon("cyberblade");
        addWeapon("flamethrower");

        addPowerup("teleporter", AmmoColor.BLUE);
        addPowerup("teleporter", AmmoColor.RED);
        addPowerup("teleporter", AmmoColor.YELLOW);
    }

    public List<ImageView> getWeapons() {
        return Arrays.asList(weapon1, weapon2, weapon3);
    }

    public List<ImageView> getPowerUps(){
        return Arrays.asList(powerup1, powerup2, powerup3);
    }

    public void addWeapon(String name)
    {
        getWeapons().get(totWeapons++).setImage(new Image("weapon/" + name + ".png"));
    }

    public void addPowerup(String name, AmmoColor color)
    {
        getPowerUps().get(totPowerups++).setImage(new Image("powerup/" + name + "_" + color.getName() + ".png"));
    }

    public void removeWeapon(String name){
        List<ImageView> weapons = getWeapons();
        for(int i=0; i < weapons.size(); i++)
            if(weapons.get(i).getImage().getUrl().contains(name)) {
                for(int j= i; j < weapons.size()-1; j++)
                    weapons.get(j).setImage(weapons.get(j+1).getImage());
                weapons.get(weapons.size()-1).setImage(null);
            }
    }

    public void removePowerup(String name, AmmoColor color){
        List<ImageView> powerups = getPowerUps();
        for(int i=0; i < powerups.size(); i++)
            if(powerups.get(i).getImage().getUrl().contains(name + "_" + color.getName())) {
                for(int j= i; j < powerups.size()-1; j++)
                    powerups.get(j).setImage(powerups.get(j+1).getImage());
                powerups.get(powerups.size()-1).setImage(null);
            }
    }

    @Override
    public HBox getRoot() {
        return cardsHBox;
    }

    public static PlayerCardsController getController(){
        return GUIView.getController("/view/ActionHandler/playerCards/playerCards.fxml", "/view/ActionHandler/playerCards/playerCards.css");
    }
}
