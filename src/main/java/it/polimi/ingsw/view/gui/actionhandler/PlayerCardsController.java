package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PrivatePlayerSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.GUIView;
import it.polimi.ingsw.view.gui.Ifxml;
import it.polimi.ingsw.view.gui.MatchSnapshotProvider;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCardsController implements Ifxml<HBox> {

    @FXML private ImageView weapon1;
    @FXML private ImageView weapon2;
    @FXML private ImageView weapon3;
    @FXML private ImageView powerup1;
    @FXML private ImageView powerup2;
    @FXML private ImageView powerup3;
    @FXML private ImageView extraWeapon;
    @FXML private ImageView extraPowerup;
    @FXML private HBox cardsHBox;
    private MatchSnapshotProvider provider;
    private PlayerColor color;
    private static final int MAX_WEAPONS = 3;
    private static final int MAX_POWERUPS = 3;
    private int totWeapons = 0, totPowerups = 0;

    public void initialize()
    {
    }

    public List<ImageView> getWeapons() {
        return Arrays.asList(weapon1, weapon2, weapon3, extraWeapon);
    }

    public List<ImageView> getPowerUps(){
        return Arrays.asList(powerup1, powerup2, powerup3, extraPowerup);
    }

    public void addLoadedWeapon(String name)
    {
        String path = "weapon/" + name + ".png";
        if(totWeapons == MAX_WEAPONS)
            extraWeapon.setImage(new Image(path));
        else
            getWeapons().get(totWeapons++).setImage(new Image(path));
    }

    public void addUnloadedWeapon(){
        addLoadedWeapon("weapon_back");
    }

    public void addUnloadedWeapon(String name){
        addLoadedWeapon(name); //todo mark card as unloaded
    }


    public void addPowerup(String name)
    {
        String path = "powerup/" + name + ".png";
        if(totPowerups == MAX_POWERUPS)
            extraWeapon.setImage(new Image(path));
        else
            getPowerUps().get(totPowerups++).setImage(new Image(path));
    }

    public void addBackPowerup(){
        addPowerup("power_up_back");
    }

    public void removeWeapon(String name){
        List<ImageView> weapons = getWeapons();
        for(int i=0; i < weapons.size(); i++)
            if(weapons.get(i).getImage().getUrl().contains(name)) {
                for (int j = i; j < weapons.size() - 1; j++)
                    weapons.get(j).setImage(weapons.get(j + 1).getImage());
                weapons.get(weapons.size() - 1).setImage(null);
                return;
            }
        extraPowerup.setImage(null);
    }

    public List<ImageView> getCards(){
        List<ImageView> ret = new ArrayList<>();
        ret.addAll(getWeapons());
        ret.addAll(getPowerUps());
        return ret;
    }

    public void removePowerup(String name, AmmoColor color){
        List<ImageView> powerups = getPowerUps();
        for(int i=0; i < powerups.size(); i++)
            if(powerups.get(i).getImage().getUrl().contains(name + "_" + color.getName())) {
                for(int j= i; j < powerups.size()-1; j++)
                    powerups.get(j).setImage(powerups.get(j+1).getImage());
                powerups.get(powerups.size()-1).setImage(null);
                return;
            }
        extraPowerup.setImage(null);
    }


    private void buildController(MatchSnapshotProvider provider, PlayerColor color){
        this.provider = provider;
        this.color = color;
        provider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot matchSnapshot){

        clear();
        for(PublicPlayerSnapshot player : matchSnapshot.getPublicPlayerSnapshot())
            if(player.color.equals(this.color.getName())) {
                onPublicPlayer(player);
                return;
            }
        onPrivatePlayer(matchSnapshot.privatePlayerSnapshot);
    }

    private String snapshotToName(String snapshotName){
        return snapshotName.replace(" ", "_").toLowerCase();
    }

    private void onPrivatePlayer(PrivatePlayerSnapshot player)
    {
        for(String weapon : player.getLoadedWeapons())
            addLoadedWeapon(snapshotToName(weapon));
        for(String weapon : player.getUnloadedWeapons())
            addUnloadedWeapon(snapshotToName(weapon));
        for(String powerup : player.getPowerUps())
            addPowerup(snapshotToName(powerup));
    }

    private void clear(){
        for(ImageView img : getCards())
            img.setImage(null);
    }

    private void onPublicPlayer(PublicPlayerSnapshot player){
        for(int i=0; i < player.loadedWeaponsNumber ; i++) //todo ask if cards are always on back
            addUnloadedWeapon();
        for(int i=0; i < player.powerUpsNumber; i++)
            addBackPowerup();
    }

    @Override
    public HBox getRoot() {
        return cardsHBox;
    }

    public static PlayerCardsController getController(MatchSnapshotProvider provider, PlayerColor color){
        PlayerCardsController ret =  GUIView.getController("/view/ActionHandler/playerCards/playerCards.fxml", "/view/ActionHandler/playerCards/playerCards.css");
        ret.buildController(provider, color);
        return ret;
    }
}
