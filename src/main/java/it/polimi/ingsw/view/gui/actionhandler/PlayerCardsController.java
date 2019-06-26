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
    private RemoteActionsProvider actionsProvider;
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

    private void addWeapon(Image weapon)
    {
        add(weapon, weapons);
    }

    private void add(Image image, List<ImageView> imageViews)
    {
        if(image == null)
            return;

        for(ImageView img : imageViews)
            if(img.getImage() != null && img.getImage().getUrl().equals(image.getUrl()))
                return;
        for(ImageView img : imageViews)
            if(img.getImage() == null)
            {
                img.setImage(image);
                Animations.appearAnimation(img);
                return;
            }
    }

    private void addPowerup(Image powerup)
    {
        add(powerup, powerups);
    }

    private Image getLoadedWeapon(String name)
    {
        String path = "weapon/" + name.replace(" ", "_").concat(".png").toLowerCase();
        return new Image(path);
    }

    private Image getBackWeapon(){
        return getLoadedWeapon("weapon_back");
    }

    private Image getUnloadedWeapon(String name){
        return getLoadedWeapon(name); //todo mark card as unloaded
    }

    private Image getPowerup(String name)
    {
        String path = "powerup/" + name.replace(" ", "_").concat(".png").toLowerCase();
        return new Image(path);
    }

    public Image getBackPowerup(){
        return getPowerup("power_up_back");
    }

    public List<ImageView> getCards(){
        List<ImageView> ret = new ArrayList<>();
        ret.addAll(getWeapons());
        ret.addAll(getPowerUps());
        return ret;
    }

    private void buildController(MatchSnapshotProvider provider, String color){
        this.provider = provider;
        this.color = color;
        provider.modelChangedEvent().addEventHandler( (a, snapshot) -> onModelChanged(snapshot));
    }

    private void onModelChanged(MatchSnapshot matchSnapshot)
    {
        List<Image> newImages = getNew(matchSnapshot);
        List<ImageView> toDelete = new ArrayList<>();
        for(ImageView cur : cards)
            if(cur.getImage() != null && newImages.stream().noneMatch(i -> i != null && i.getUrl().equals(cur.getImage().getUrl())))
                toDelete.add(cur);

        if(!toDelete.isEmpty())
        {
            ImageView first = toDelete.get(0);
            Animations.disappearAnimation(first, () -> Platform.runLater(() -> {
                toDelete.forEach(i -> i.setImage(null));
                addCards(newImages);
            }));
            for(int i=1; i < toDelete.size(); i++)
                Animations.disappearAnimation(toDelete.get(i), () -> {});
        }
        else
            addCards(newImages);
    }

    private void addCards(List<Image> cards)
    {
        for(int i=0; i < MAX_WEAPONS; i++)
            addWeapon(cards.get(i));
        for(int i=0; i < MAX_POWERUPS ; i++)
            addPowerup(cards.get(i + MAX_WEAPONS));
        addWeapon(cards.get(MAX_WEAPONS + MAX_POWERUPS - 2));
        addPowerup(cards.get(MAX_WEAPONS + MAX_POWERUPS - 1));
    }

    private List<Image> getNew(MatchSnapshot matchSnapshot)
    {
        for(PublicPlayerSnapshot player : matchSnapshot.getPublicPlayerSnapshot())
            if(player.color.equals(this.color))
                return onPublicPlayer(player);

        return onPrivatePlayer(matchSnapshot.privatePlayerSnapshot);
    }

    private List<String> getBackup()
    {
        List<String> ret = new ArrayList<>();
        for(ImageView card : cards)
            if(card.getImage() != null)
                ret.add(card.getImage().getUrl());
            else
                ret.add(null);
        return ret;
    }

    public static String snapshotToName(String snapshotName){
        return snapshotName.replace(" ", "_").toLowerCase();
    }

    private List<Image> onPrivatePlayer(PrivatePlayerSnapshot player)
    {
        Image[] ret = new Image[MAX_POWERUPS + MAX_WEAPONS + 2];
        List<String> loadedWeapons = player.getLoadedWeapons();
        List<String> unloaded = player.getUnloadedWeapons();
        List<String> powerups = player.getPowerUps();
        int tot = MAX_POWERUPS + MAX_WEAPONS;

        for(int i=0; i < MAX_WEAPONS + MAX_POWERUPS + 2; i++)
            ret[i] = null;

        int j=0;
        for(String weapon : loadedWeapons)
            ret[j == MAX_WEAPONS ? tot-2 : j++] = getLoadedWeapon(weapon);
        for(String weapon: unloaded)
            ret[j == MAX_WEAPONS ? tot-2 : j++] = getUnloadedWeapon(weapon);

        j=MAX_WEAPONS;
        for(String powerup : player.getPowerUps())
            ret[j == tot-2 ? tot-1 : j++] = getPowerup(powerup);
        return Arrays.asList(ret);
    }

    private void clear(){
        totWeapons = totPowerups = 0;
        for(ImageView img : getCards())
            img.setImage(null);
    }

    private List<Image> onPublicPlayer(PublicPlayerSnapshot player){
        Image[] ret = new Image[8];
        List<String> loadedWeapons = player.getUnloadedWeapons();
        int unloaded = player.loadedWeaponsNumber;
        int powerups = player.powerUpsNumber;

        int tot = MAX_POWERUPS + MAX_WEAPONS;

        for(int i=0; i < MAX_WEAPONS + MAX_POWERUPS + 2; i++)
            ret[i] = null;

        int j=0;
        for(String weapon : loadedWeapons)
            ret[j == MAX_WEAPONS ? tot-2 : j++] = getLoadedWeapon(weapon);
        for(int i=0; i < unloaded; i++)
            ret[j == MAX_WEAPONS ? tot-2 : j++] = getBackWeapon();

        j=MAX_WEAPONS;
        for(int i=0; i < powerups; i++)
            ret[j == tot-2 ? tot-1 : j++] = getBackPowerup();
        return Arrays.asList(ret);
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
