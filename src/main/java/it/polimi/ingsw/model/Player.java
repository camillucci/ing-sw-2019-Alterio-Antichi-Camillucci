package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represent a single playing Player, it contains all infos available to the player
 */
public class Player {

    /**
     * The event that is invoked when a Player is damaged, it notifies the Match
     */
    public final IEvent<Player, Integer> damagedEvent;
    /**
     * The event that is invoked when a Player becomes dead, it notifies the Match
     */
    public final IEvent<Player, Player> deathEvent;
    /**
     * The GameBoard the Player is currently playing in
     */
    public final GameBoard gameBoard;
    /**
     * The name of the Player, it's unique in the Match the Player is currently playing in
     */
    public final String name;
    /**
     * The color of the Player (one among Blue, Green, Grey, Violet, Yellow)
     */
    public final PlayerColor color;
    /**
     * The score a Player currently got
     */
    private int points = 0;
    /**
     * The number of skulls the Player currently have, it identify how many time it has died and how many points it gives
     */
    private int skull = 0;
    /**
     * The set of ammo the Player have, for each color (Blue, Red, Yellow) a number from 0 to 3
     */
    private Ammo ammo = new Ammo(1, 1, 1);
    /**
     * The Square the Player is currently in
     */
    private Square currentSquare = null;
    /**
     * It states if the Player has died after the final frenzy has been activated
     */
    private boolean finalFrenzy;
    /**
     * The list of damages, in the form of PlayerColor, the Player has, the length is between 0 and 12
     */
    private List<PlayerColor> damage = new ArrayList<>();
    /**
     * The list of marks, in the form of PlayerColor, the Player has, the length is between 0 and 12
     */
    private List<PlayerColor> mark = new ArrayList<>();
    /**
     * The list of loaded weapons the Player has, which are the weapons ready to shoot, the length is between 0 and 3
     */
    private List<WeaponCard> loadedWeapons = new ArrayList<>();
    /**
     * The list of unloaded weapons the Player has, which are the weapons that cannot shoot until reloaded, the length is between 0 and 3
     */
    private List<WeaponCard> unloadedWeapons = new ArrayList<>();
    /**
     * The set of powerUps the Player has, it contains a total of 3 PowerUpCard among EndStart, InTurn and CounterAttack
     */
    private PowerUpSet powerupSet = new PowerUpSet();
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_POWER_UPS_RESPAWN = 4;
    private static final int MAX_MARKS = 3;
    private static final int MAX_DAMAGES = 12;

    /**
     * The constructor create a new Player given the name, the color and the GameBoard,
     * it also sets at a default value the various attributes
     * @param name The name of the Player
     * @param color The color of the Player
     * @param gameBoard The GameBoard the Player is playing in
     */
    public Player (String name, PlayerColor color, GameBoard gameBoard) {

        this.name = name;
        this.color = color;
        this.gameBoard = gameBoard;
        this.finalFrenzy = false;
        this.damagedEvent = new Event<>();
        this.deathEvent = new Event<>();
    }

    /**
     * This constructor is a copy constructor, it create a new Player that is the copy of a given one
     * @param player The Player that has to be copied
     * @param clonedGameBoard The already cloned GameBoard
     * @param clonedSquare The already cloned Square
     */
    public Player(Player player, GameBoard clonedGameBoard, Square clonedSquare) {
        this.gameBoard = clonedGameBoard;
        this.currentSquare = clonedSquare;

        this.damagedEvent = player.damagedEvent;
        this.deathEvent = player.deathEvent;
        this.name = player.name;
        this.color = player.color;
        this.points = player.points;
        this.skull = player.skull;
        this.ammo = new Ammo(player.ammo);
        this.finalFrenzy = player.finalFrenzy;
        this.damage = new ArrayList<>(player.damage);
        this.mark = new ArrayList<>(player.mark);
        this.loadedWeapons = new ArrayList<>(player.loadedWeapons);
        this.unloadedWeapons = new ArrayList<>(player.unloadedWeapons);
        this.powerupSet = new PowerUpSet(player.powerupSet);
    }

    /**
     * This method adds or subtract blue ammo from the Player, blue ammo must always be a number between 0 and 3
     * @param val The number of blu ammo to addTarget to the Player, a negative value will subtract them
     */
    public void addBlue(int val){
        if(ammo.blue + val > MAX_AMMO)
            ammo = new Ammo(MAX_AMMO, ammo.red, ammo.yellow);
        else
            ammo = new Ammo(ammo.blue + val, ammo.red, ammo.yellow);
    }

    /**
     * This method adds or subtract red ammo from the Player, red ammo must always be a number between 0 and 3
     * @param val The number of red ammo to addTarget to the Player, a negative value will subtract them
     */
    public void addRed(int val){
        if(ammo.red + val > MAX_AMMO)
            ammo = new Ammo(ammo.blue, MAX_AMMO, ammo.yellow);
        else
            ammo = new Ammo(ammo.blue, ammo.red + val, ammo.yellow);
    }

    /**
     * This method adds or subtract yellow ammo from the Player, yellow ammo must always be a number between 0 and 3
     * @param val The number of yellow ammo to addTarget to the Player, a negative value will subtract them
     */
    public void addYellow(int val){
        if(ammo.yellow + val > MAX_AMMO)
            ammo = new Ammo(ammo.blue, ammo.red, MAX_AMMO);
        else
            ammo = new Ammo(ammo.blue, ammo.red, ammo.yellow + val);
    }

    /**
     * This method draws a PowerUpCard from the PowerUpDeck and assign it to the Player,
     * with this method a Player cannot have more than 3 PowerUpCards at the same time
     */
    public void addPowerUpCard() {
        if(powerupSet.getAll().size() < MAX_POWER_UPS){
            powerupSet.add(gameBoard.getPowerupDeck().draw());
        }
    }

    /**
     * This method draws a PowerUpCard from the PowerUpDeck and assign it to the Player,
     * with this method a Player can have a fourth PowerUpCard, but it will be discarded at the respawn
     */
    public void addPowerUpCardRespawn() {
        if(powerupSet.getAll().size() < MAX_POWER_UPS_RESPAWN){
            powerupSet.add(gameBoard.getPowerupDeck().draw());
        }
    }

    /**
     * This method adds a specific PowerUpCard to the Player
     * @param powerUpCard The card to addTarget to the Player
     */
    public void addPowerUpCard(PowerUpCard powerUpCard) { // Only for tests
        powerupSet.add(powerUpCard);
    }

    /**
     * This method remove a specific PowerUpCard from the Player
     * @param powerUpCard The card to remove from the Player
     */
    public void removePowerUpCard(PowerUpCard powerUpCard)
    {
        powerupSet.remove(powerUpCard);
    }

    /**
     * This method adds damages to the Player and converts marks of the same color into damages
     * @param shooter The Player who is causing the damages
     * @param val The quantity of damages received by the Player
     */
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
            mark.remove(mark.get(i));
        ((Event<Player, Integer>)this.damagedEvent).invoke(this, val);
        if(damage.size() >= 11)
            ((Event<Player, Player>)this.deathEvent).invoke(this, shooter);
    }

    /**
     * This method adds damages to the Player but does not converts marks of the same color into damages
     * @param shooter The Player who is causing the damages
     * @param val The quantity of damages received by the Player
     */
    public void addDamageNoMarks(Player shooter, int val) {
        for (int i = 0; i < val && damage.size() < MAX_DAMAGES; i++)
            damage.add(shooter.color);
    }

    /**
     * This method adds marks to the Player, the number of marks can be maximum 3 for each color
     * @param shooter The Player who is causing the marks
     * @param val The quantity of marks received by the Player
     */
    public void addMark(Player shooter, int val) {
        int temp = 0;
        for (PlayerColor p : mark)
            if (p == shooter.color)
                temp++;

        for (int i = 0; i < val && temp < MAX_MARKS; i++, temp++)
            mark.add(shooter.color);
    }

    /**
     * This method adds a specific weapon to the Player
     * @param weaponCard The weapon to be added
     */
    public void addWeapon(WeaponCard weaponCard) {
        loadedWeapons.add(weaponCard);
    }

    /**
     * This method removes a specifis weapon from the Player
     * @param weaponCard The weapon to be removed
     */
    public void removeWeapon(WeaponCard weaponCard) {
        if(loadedWeapons.contains(weaponCard))
            loadedWeapons.remove(weaponCard);
        else
            unloadedWeapons.remove(weaponCard);
    }

    /**
     * This method moves a weapon from the loaded list to the unloaded list
     * @param weaponCard The weapon to be moved
     */
    public void reloadWeapon(WeaponCard weaponCard) {
        loadedWeapons.add(unloadedWeapons.remove(unloadedWeapons.indexOf(weaponCard)));
    }

    /**
     * This method moves a weapon from the unloaded list to the loaded list
     * @param weaponCard The weapon to be moved
     */
    public void unloadWeapon(WeaponCard weaponCard) {
        unloadedWeapons.add(loadedWeapons.remove(loadedWeapons.indexOf(weaponCard)));
    }

    /**
     * This method adds the points the Player has gained through a kill
     * @param newPoints The quantity of points to be added
     */
    public void addPoints(int newPoints) {
        points = points + newPoints;
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

    /**
     * This method returns the total number of ammo contained in the ammo set
     * @return The total number of ammo contained in the ammo set
     */
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

    /**
     * This method returns all the weapons the Player has, both loaded and unloaded
     * @return A list that contains all the weapons the Player has, both loaded and unloaded
     */
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

    public PowerUpSet getPowerupSet() {
        return powerupSet;
    }

    public List<PlayerColor> getMark() {
        return mark;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public void setSkull(int skull) {
        this.skull = skull;
    }

    /**
     * This method sets the finalFrenzy value to true when a player dies during the final frenzy
     */
    public void setFinalFrenzy() {
        this.finalFrenzy = true;
    }

    /**
     * This method returns a list of Ammo that can be discarded with the Targeting Scope PowerUpCard
     * @return A list of Ammo that can be discarded with the Targeting Scope PowerUpCard
     */
    public List<Ammo> getDiscardableAmmo() {
        List<Ammo> temp = new ArrayList<>();
        if(ammo.blue >= 1)
            temp.add(new Ammo(1, 0, 0));
        if(ammo.red >= 1)
            temp.add(new Ammo(0, 1, 0));
        if(ammo.yellow >= 1)
            temp.add(new Ammo(0, 0, 1));
        return temp;
    }
}
