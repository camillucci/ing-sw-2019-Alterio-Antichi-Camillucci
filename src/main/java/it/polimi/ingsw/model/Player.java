package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player implements Cloneable {

    public final Event<Player, Integer> damagedEvent = new Event<>();
    public final Event<Player, Integer> markedEvent = new Event<>();
    public final Event<Player, Player> deathEvent = new Event<>();
    public final GameBoard gameBoard;
    public final PlayerColor color;
    public final String name;
    private int points;
    private int skull;
    private int blueAmmo;
    private int redAmmo;
    private int yellowAmmo;
    private Square currentSquare;
    private boolean finalFrenzy;
    private ArrayList<PlayerColor> damage = new ArrayList<>();
    private ArrayList<PlayerColor> mark = new ArrayList<>();
    private ArrayList<WeaponCard> loadedWeapons = new ArrayList<>();
    private ArrayList<WeaponCard> unloadedWeapons = new ArrayList<>();
    private ArrayList<PowerUpCard> powerUps = new ArrayList<>();
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_POWER_UPS_RESPAWN = 4;
    private static final int MAX_MARKS = 3;
    private static final int MAX_DAMAGES = 12;
    private static Logger logger = Logger.getLogger("client");


    public Player (String name, PlayerColor color, GameBoard gameBoard) {

        this.name = name;
        this.points = 0;
        this.blueAmmo = 1;
        this.yellowAmmo = 1;
        this.redAmmo = 1;
        this.skull = 0;
        this.color = color;
        this.gameBoard = gameBoard;
        this.finalFrenzy = false;
    }

    public void addBlue(int val){
        blueAmmo = blueAmmo + val;
        if(blueAmmo >= MAX_AMMO) {
            blueAmmo = MAX_AMMO;
        }
    }

    public void addRed(int val){
        redAmmo = redAmmo + val;
        if(redAmmo >= MAX_AMMO) {
            redAmmo = MAX_AMMO;
        }
    }

    public void addYellow(int val){
        yellowAmmo = yellowAmmo + val;
        if(yellowAmmo >= MAX_AMMO) {
            yellowAmmo = MAX_AMMO;
        }
    }

    public void addPowerUpCard() {
        if(powerUps.size() < MAX_POWER_UPS){
            powerUps.add(gameBoard.getPowerUpDeck().draw());
        }
    }

    public void addPowerUpCardRespawn() {
        if(powerUps.size() < MAX_POWER_UPS_RESPAWN){
            powerUps.add(gameBoard.getPowerUpDeck().draw());
        }
    }

    public void removePowerUpCard(PowerUpCard powerUpCard)
    {
        powerUps.remove(powerUpCard);
    }

    public void addDamage(Player shooter, int val) {
        for (int i = 0; i < val && damage.size() < MAX_DAMAGES; i++)
            damage.add(shooter.color);

        List<Integer> temp = new ArrayList<>();
        for(int i = 0; i < mark.size() && damage.size() < MAX_DAMAGES; i++ )
            if (shooter.color == mark.get(i)) {
                damage.add(shooter.color);
                temp.add(i);
            }

        for(int i = temp.size() - 1; i >= 0; i--)
            mark.remove(i);
        this.damagedEvent.invoke(this, val);
    }

    public void addMark(Player shooter, int val) {
        int temp = 0;
        for (PlayerColor p : mark)
            if (p == shooter.color)
                temp++;

        for (int i = 0; i < val && temp < MAX_MARKS; i++, temp++)
            mark.add(shooter.color);
        this.markedEvent.invoke(this, val);
    }

    public void addWeapon(WeaponCard weaponCard) {
        loadedWeapons.add(weaponCard);
    }

    public void removeWeapon(WeaponCard weaponCard) {
        if(loadedWeapons.contains(weaponCard))
            loadedWeapons.remove(weaponCard);
        else
            unloadedWeapons.remove(weaponCard);
    }

    public void reloadWeapon(WeaponCard weaponCard) {
        loadedWeapons.add(unloadedWeapons.remove(unloadedWeapons.indexOf(weaponCard)));
    }

    public void unloadWeapon(WeaponCard weaponCard) {
        unloadedWeapons.add(loadedWeapons.remove(loadedWeapons.indexOf(weaponCard)));
    }

    public void addPoints(int newPoints) {
        points = points + newPoints;
    }

    public Player getClone() { //throws CloneNotSupportedException
        try {
            Player p = (Player)this.clone();
            p.damage = new ArrayList<>(this.damage);
            p.mark = new ArrayList<>(this.mark);
            loadedWeapons = new ArrayList<>(this.loadedWeapons);
            unloadedWeapons = new ArrayList<>(this.unloadedWeapons);
            powerUps = new ArrayList<>(this.powerUps);
            return p;
        }
        catch(CloneNotSupportedException cNSE){
            logger.log(Level.INFO, "CloneNotSupportedException, Class Match, Line 158");
        }
        return null;
    }

    public int getPoints() {
        return points;
    }

    public int getSkull() {
        return skull;
    }

    public int getBlueAmmo() {
        return blueAmmo;
    }

    public int getRedAmmo() {
        return redAmmo;
    }

    public int getYellowAmmo() {
        return yellowAmmo;
    }

    public int getTotalAmmo() {
        return blueAmmo + redAmmo + yellowAmmo;
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public boolean isFinalFrenzy() {
        return finalFrenzy;
    }

    public List<WeaponCard> getLoadedWeapons(){
        return this.loadedWeapons;
    }

    public List<WeaponCard> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public List<WeaponCard> getWeapons()
    {
        return Stream.concat(loadedWeapons.stream(), unloadedWeapons.stream()).collect(Collectors.toList());
    }

    public List<PowerUpCard> getPowerUps() {
        return powerUps;
    }

    public List<PlayerColor> getDamage() {
        return damage;
    }

    public List<PlayerColor> getMark() {
        return mark;
    }

    public String getName() {
        return name;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public void setFinalFrenzy(boolean finalFrenzy) {
        this.finalFrenzy = finalFrenzy;
    }
}
