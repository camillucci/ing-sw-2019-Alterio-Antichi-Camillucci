package it.polimi.ingsw.view.gui.actionhandler;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PrivatePlayerSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.gui.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerCardsController implements Ifxml<HBox>
{
    public final IEvent<PlayerCardsController, String> addWeaponEvent = new Event<>();
    public final IEvent<PlayerCardsController, String> addPowerupEvent = new Event<>();
    public final IEvent<PlayerCardsController, String> usePowerupEvent = new Event<>();
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
    private RemoteActionsProvider actionProvider;
    private String draggingPowerup;
    private boolean isOut = false;

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

    private ImageView addWeapon(String name, int rotationAngle)
    {
        for(ImageView weapon : getWeapons())
            if(weapon.getImage() == null)
            {
                weapon.setImage(Cache.getImage("weapon/" + snapshotToFileName(name)));
                weapon.setRotate(rotationAngle);
                Animations.appearAnimation(weapon);
                return weapon;
            }
        throw new RuntimeException("cards set is full");
    }

    private ImageView addWeapon(String name)
    {
       return addWeapon(name, 0);
    }

    private ImageView addUnloadedWeapon(String name){
        return addWeapon(name, 180);
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
                powerup.setImage(Cache.getImage("powerup/" + snapshotToFileName(name)));
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

    private void onNewAction(RemoteAction action) {
        cards.forEach(c -> c.setDisable(true));
        RemoteAction.Data data = action.getData();
        setupAddWeapon(data);
        setupAddPowerup(data);
        setupUsePowerup(data);
    }

    private <T> void invokeEvent(IEvent<PlayerCardsController, T> event, T arg)
    {
        ((Event<PlayerCardsController, T>)event).invoke(this, arg);
    }

    private void setupEvents() {
        getRoot().setOnMouseExited(e -> isOut = true);
        getRoot().setOnMouseEntered(e -> isOut = false);
    }

    private List<ImageView> getAll(String name){
        return cards.stream().filter(i -> i.getImage() != null && i.getImage().getUrl().toLowerCase().contains(snapshotToName(name))).collect(Collectors.toList());
    }

    private void setupAddWeapon(RemoteAction.Data data)
    {
        for(String weapon : data.getPossibleWeapons())
            getAll(weapon).forEach(w -> enableEnd( () -> w.setOnMouseClicked(e -> invokeEvent(addWeaponEvent, weapon))));
    }

    private void setupUsePowerup(RemoteAction.Data data)
    {
        for(String powerup : data.getPossiblePowerUps())
            getAll(powerup).forEach(p -> enableEnd( () -> p.setOnMouseClicked(e -> invokeEvent(usePowerupEvent, powerup))));
    }

    private void enableEnd(Runnable func)
    {
        cards.forEach(c -> c.setDisable(false));
        func.run();
    }

    private void setupAddPowerup(RemoteAction.Data data)
    {
        for(String powerup : data.getDiscardablePowerUps())
            for(ImageView pu : getAll(powerup))
            {
                pu.setDisable(false);
                pu.setOnMouseDragged(e -> draggingPowerup = powerup);
                pu.setOnMouseReleased(e -> {
                    if(isOut)
                        invokeEvent(addPowerupEvent, powerup);
                    draggingPowerup = null;
                });
            }
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

    public static String snapshotToName(String snapshotName)
    {
        return snapshotName.replace(" ", "_").toLowerCase();
    }

    public static String snapshotToFileName(String snapshotName){
        return snapshotToName(snapshotName).concat(".png").toLowerCase();
    }

    private boolean startDeletingAnimation(PrivatePlayerSnapshot player, List<String> oldList, List<String> newList, boolean found)
    {
        List<String> tmp = oldList.stream().distinct().collect(Collectors.toList());
        for(String card : tmp)
        {
            List<ImageView> occurencies = getCards(card);
            for (int i = 0; i < (Collections.frequency(oldList, card) - Collections.frequency(newList, card)); i++)
                if (!found) {
                    found = true;
                    Animations.disappearAnimation(occurencies.get(i), () -> {
                        removeCardsPrivatePlayer(player);
                        addCardPrivatePlayer(player);
                    });
                } else Animations.disappearAnimation(occurencies.get(i), () -> {
                });
        }
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

    private void delete(List<String> oldList, List<String> newList)
    {
        List<String> tmp = oldList.stream().distinct().collect(Collectors.toList());
        for(String s: tmp)
        {
            List<ImageView> occ = getCards(s);
            for (int i = 0; i < (Collections.frequency(oldList, s) - Collections.frequency(newList, s)); i++)
                occ.get(i).setImage(null);
        }
    }

    private void removeCardsPrivatePlayer(PrivatePlayerSnapshot player)
    {
        if(old != null)
        {
            delete(old.privatePlayerSnapshot.getLoadedWeapons(), player.getLoadedWeapons());
            delete(old.privatePlayerSnapshot.getUnloadedWeapons(), player.getUnloadedWeapons());
            delete(old.privatePlayerSnapshot.getPowerUps(), player.getPowerUps());
        }
    }

    private void addCardPrivatePlayer(PrivatePlayerSnapshot player)
    {
        for(String w : player.getLoadedWeapons())
            if(old == null)
                addWeapon(w);
            else
                for(int i=0; i < Collections.frequency(player.getLoadedWeapons(), w) - Collections.frequency(old.privatePlayerSnapshot.getLoadedWeapons(), w); i++)
                    addWeapon(w);
        for(String w : player.getUnloadedWeapons())
            if(old == null)
                addUnloadedWeapon(w);
            else
                for(int i=0; i < Collections.frequency(player.getUnloadedWeapons(), w) - Collections.frequency(old.privatePlayerSnapshot.getUnloadedWeapons(), w); i++)
                    addUnloadedWeapon(w);
        for(String pu : player.getPowerUps())
            if(old == null)
                addPowerup(pu);
            else
                for(int i=0; i < Collections.frequency(player.getPowerUps(), pu) - Collections.frequency(old.privatePlayerSnapshot.getPowerUps(), pu); i++)
                    addPowerup(pu);
        old = matchSnapshot;
    }

    private List<ImageView> getCards(String name)
    {
        return cards.stream().filter(card ->  card.getImage() != null && card.getImage().getUrl().contains(name.replace(" ", "_").toLowerCase())).collect(Collectors.toList());
    }

    private void onPublicPlayer(PublicPlayerSnapshot player){
        clear();
        player.getUnloadedWeapons().forEach(this::addWeapon);
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


    private void buildController(MatchSnapshotProvider provider, RemoteActionsProvider actionsProvider, String color){
        this.provider = provider;
        this.actionProvider = actionsProvider;
        this.color = color;
        provider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
        if(actionsProvider != null) {
            actionsProvider.newActionsEvent().addEventHandler((a, actions) -> onNewAction(actions));
            actionsProvider.notifyingToServeEvent().addEventHandler((a,b) -> disable());
            setupEvents();
        }
    }

    private void disable() {
        cards.forEach(card -> card.setDisable(true));
    }

    public static PlayerCardsController getController(MatchSnapshotProvider provider, String color)
    {
       return getController(provider, null, color);
    }

    public static PlayerCardsController getController(MatchSnapshotProvider provider, RemoteActionsProvider actionsProvider, String color){
        PlayerCardsController ret =  GUIView.getController("/view/ActionHandler/playerCards/playerCards.fxml", "/view/ActionHandler/playerCards/playerCards.css");
        ret.buildController(provider, actionsProvider, color);
        return ret;
    }
}
