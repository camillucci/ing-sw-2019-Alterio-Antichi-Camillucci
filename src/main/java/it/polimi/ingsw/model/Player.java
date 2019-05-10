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
    private int points = 0;
    private int skull = 0;
    private Ammo ammo = new Ammo(1, 1, 1);
    private Square currentSquare;
    private boolean finalFrenzy;
    private List<PlayerColor> damage = new ArrayList<>();
    private List<PlayerColor> mark = new ArrayList<>();
    private List<WeaponCard> loadedWeapons = new ArrayList<>();
    private List<WeaponCard> unloadedWeapons = new ArrayList<>();
    private PowerUpSet powerupSet = new PowerUpSet();
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_POWER_UPS_RESPAWN = 4;
    private static final int MAX_MARKS = 3;
    private static final int MAX_DAMAGES = 12;
    private static Logger logger = Logger.getLogger("client");


    public Player (String name, PlayerColor color, GameBoard gameBoard) {

        this.name = name;
        this.color = color;
        this.gameBoard = gameBoard;
        this.finalFrenzy = false;
    }

    public void addBlue(int val){
        if(ammo.blue + val > MAX_AMMO)
            ammo = new Ammo(MAX_AMMO, ammo.red, ammo.yellow);
        else
            ammo = new Ammo(ammo.blue + val, ammo.red, ammo.yellow);
    }

    public void addRed(int val){
        if(ammo.red + val > MAX_AMMO)
            ammo = new Ammo(ammo.blue, MAX_AMMO, ammo.yellow);
        else
            ammo = new Ammo(ammo.blue, ammo.red + val, ammo.yellow);
    }

    public void addYellow(int val){
        if(ammo.yellow + val > MAX_AMMO)
            ammo = new Ammo(ammo.blue, ammo.red, MAX_AMMO);
        else
            ammo = new Ammo(ammo.blue, ammo.red, ammo.yellow + val);
    }

    public void addPowerUpCard() {
        if(powerupSet.getAll().size() < MAX_POWER_UPS){
            powerupSet.add(gameBoard.powerupDeck.draw());
        }
    }

    public void addPowerUpCardRespawn() {
        if(powerupSet.getAll().size() < MAX_POWER_UPS_RESPAWN){
            powerupSet.add(gameBoard.powerupDeck.draw());
        }
    }

    public void removePowerUpCard(PowerUpCard powerUpCard)
    {
        powerupSet.remove(powerUpCard);
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
            p.loadedWeapons = new ArrayList<>(this.loadedWeapons);
            p.unloadedWeapons = new ArrayList<>(this.unloadedWeapons);
            p.powerupSet = new PowerUpSet(powerupSet);
            return p;
        }
        catch(CloneNotSupportedException e){
            logger.log(Level.INFO, "CloneNotSupportedException, Class Match, Line 152", e);
        }
        return null;
    }

    public int getPoints() {
        return points;
    }

    public int getSkull() {
        return skull;
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public int getTotalAmmo() {
        return ammo.blue + ammo.red + ammo.yellow;
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
        return powerupSet.getAll();
    }

    public List<PlayerColor> getDamage() {
        return damage;
    }

    public PowerUpSet getPowerupSet() {return powerupSet;}

    public List<PlayerColor> getMark() {
        return mark;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public void setFinalFrenzy(boolean finalFrenzy) {
        this.finalFrenzy = finalFrenzy;
    }

    public List<Ammo> getDiscardableAmmo() {
        List<Ammo> temp = new ArrayList<>();
        if(ammo.blue > 1)
            temp.add(new Ammo(1, 0, 0));
        if(ammo.red > 1)
            temp.add(new Ammo(0, 1, 0));
        if(ammo.yellow > 1)
            temp.add(new Ammo(0, 0, 1));
        return temp;
    }
}
