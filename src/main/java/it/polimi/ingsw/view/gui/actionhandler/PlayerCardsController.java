package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PrivatePlayerSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.view.gui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private MatchSnapshot old = null;
    private MatchSnapshot matchSnapshot;
    private String color;
    private List<ImageView> cards;
    private List<ImageView> weapons;
    private List<ImageView> powerups;
    private static final int MAX_WEAPONS = 3;
    private static final int MAX_POWERUPS = 3;
    private int totWeapons = 0, totPowerups = 0;

    public List<ImageView> getWeapons() {
        return Arrays.asList(weapon1, weapon2, weapon3, extraWeapon);
    }

    public List<ImageView> getPowerUps(){
        return Arrays.asList(powerup1, powerup2, powerup3, extraPowerup);
    }

    public void initialize(){
        cards = getCards();
        powerups = getPowerUps();
        weapons = getWeapons();
    }

    private ImageView addWeapon(String name)
    {
        for(ImageView weapon : getWeapons())
            if(weapon.getImage() == null)
            {
                weapon.setImage(new Image("weapon/" + snapshotToName(name)));
                Animations.appearAnimation(weapon);
                return weapon;
            }
        return null;
    }

    private void addBackWeapon(){
        addWeapon("weapon_back");
    }

    private void addBackPowerup(){
        addPowerup("power_up_back");
    }

    private ImageView addPowerup(String name)
    {
        for(ImageView powerup : getPowerUps())
            if(powerup.getImage() == null)
            {
                powerup.setImage(new Image("powerup/" + snapshotToName(name)));
                Animations.appearAnimation(powerup);
                return powerup;
            }
        return null;
    }

    public List<ImageView> getCards(){
        List<ImageView> ret = new ArrayList<>();
        ret.addAll(getWeapons());
        ret.addAll(getPowerUps());
        return ret;
    }

    private void removeAll(List<String> names)
    {
        for(String x : names)
            for(ImageView img : getCards())
                if(img.getImage() != null && img.getImage().getUrl().contains(x.replace(" ", "_").toLowerCase()))
                    img.setImage(null);
    }

    private void buildController(MatchSnapshotProvider provider, String color){
        this.provider = provider;
        this.color = color;
        provider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot matchSnapshot)
    {
        this.matchSnapshot = matchSnapshot;
        if(matchSnapshot.privatePlayerSnapshot.color.equals(this.color))
            onPrivatePlayer(matchSnapshot.privatePlayerSnapshot);
        else
            for(PublicPlayerSnapshot p : matchSnapshot.getPublicPlayerSnapshot())
                if(p.color.equals(this.color))
                    onPublicPlayer(p);
    }

    public static String snapshotToName(String snapshotName){
        return snapshotName.replace(" ", "_").concat(".png").toLowerCase();
    }

    private boolean startDeletingAnimation(PrivatePlayerSnapshot player, List<String> oldList, List<String> newList, boolean found)
    {
        for(String loaded : oldList)
            if(!newList.contains(loaded))
                if(!found)
                {
                    found = true;
                    Animations.disappearAnimation(getCard(loaded), () -> {
                        removeCardsPrivatePlayer(player);
                        addCardPrivatePlayer(player);
                    });
                }
                else Animations.disappearAnimation(getCard(loaded), () -> {});
        return found;
    }

    private void onPrivatePlayer(PrivatePlayerSnapshot player)
    {
        boolean found = false;
        if(old != null)
        {
            PrivatePlayerSnapshot oldP = old.privatePlayerSnapshot;
            found = startDeletingAnimation(player, oldP.getLoadedWeapons(), player.getLoadedWeapons(), found);
            found = startDeletingAnimation(player, oldP.getUnloadedWeapons(), player.getUnloadedWeapons(), found);
            found = startDeletingAnimation(player, oldP.getPowerUps(), player.getPowerUps(), found);
        }
        if(!found)
        {
            removeCardsPrivatePlayer(player);
            addCardPrivatePlayer(player);
        }
    }

    private void removeCardsPrivatePlayer(PrivatePlayerSnapshot player)
    {
        if(old != null) {
            removeAll(old.privatePlayerSnapshot.getLoadedWeapons().stream().filter(c -> !player.getLoadedWeapons().contains(c)).collect(Collectors.toList()));
            removeAll(old.privatePlayerSnapshot.getUnloadedWeapons().stream().filter(c -> !player.getUnloadedWeapons().contains(c)).collect(Collectors.toList()));
            removeAll(old.privatePlayerSnapshot.getPowerUps().stream().filter(c -> !player.getPowerUps().contains(c)).collect(Collectors.toList()));
        }
    }

    private void addCardPrivatePlayer(PrivatePlayerSnapshot player)
    {
        player.getLoadedWeapons().stream().filter(c -> old == null || !old.privatePlayerSnapshot.getLoadedWeapons().contains(c)).forEach(this::addWeapon);
        player.getUnloadedWeapons().stream().filter(c -> old == null || !old.privatePlayerSnapshot.getUnloadedWeapons().contains(c)).forEach(this::addWeapon);
        player.getPowerUps().stream().filter(c -> old == null ||  !old.privatePlayerSnapshot.getPowerUps().contains(c)).forEach(this::addPowerup);
        old = matchSnapshot;
    }

    private ImageView getCard(String name)
    {
        for(ImageView card : getCards())
            if(card.getImage() != null && card.getImage().getUrl().contains(name.replace(" ", "_").toLowerCase()))
                return card;
        return null;
    }

    private void onPublicPlayer(PublicPlayerSnapshot player){
        clear();
        player.getUnloadedWeapons().forEach(card -> addWeapon(card));
        for(int i=0; i < player.loadedWeaponsNumber; i++)
            addBackWeapon();
        for(int i = 0; i < player.powerUpsNumber; i++)
            addBackPowerup();
    }

    private void clear(){
        getCards().forEach(i -> i.setImage(null));
    }

    @Override
    public HBox getRoot() {
        return cardsHBox;
    }

    public static PlayerCardsController getController(MatchSnapshotProvider provider, String color){
        PlayerCardsController ret =  GUIView.getController("/view/ActionHandler/playerCards/playerCards.fxml", "/view/ActionHandler/playerCards/playerCards.css");
        ret.buildController(provider, color);
        return ret;
    }
}
