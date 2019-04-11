package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.powerups.TagbackGrenade;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private PlayerColor color;
    private int points;
    private int skull;
    private int blueAmmo;
    private int redAmmo;
    private int yellowAmmo;
    private GameBoard gameBoard;
    private Square currentSquare;
    private boolean finalFrenzy;
    private ArrayList<PlayerColor> damage = new ArrayList<>();
    private ArrayList<PlayerColor> mark = new ArrayList<>();
    private ArrayList<WeaponCard> loadedWeapons = new ArrayList<>();
    private ArrayList<WeaponCard> unloadedWeapons = new ArrayList<>();
    private ArrayList<PowerUpCard> powerUps = new ArrayList<>();
    public final Event<Player, Player> deathEvent = new Event<>();
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_MARKS = 3;
    private static final int MAX_DAMAGES = 12;


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

    public void addDamage(Player shooter, int val) {
        for (int i = 0; i < val && damage.size() < MAX_DAMAGES; i++)
            damage.add(shooter.getColor());

        int temp = mark.size();
        for(int i = 0; i < temp && damage.size() < MAX_DAMAGES; i++ )
            if(shooter.getColor() == mark.get(i)) {
                damage.add(shooter.getColor());
                mark.remove(i);
                i--;
                temp--;
            }

        for(PowerUpCard pu : powerUps){
            if(pu instanceof TagbackGrenade){
                pu.buildFireModality();
            }
        }
    }

    public void addMark(Player shooter, int val) {
        int temp = 0;
        for (int i = 0; i < mark.size(); i++)
            if(mark.get(i) == shooter.getColor())
                temp++;

        for (int i = 0; i < val && temp < MAX_MARKS; i++, temp++) {
            mark.add(shooter.getColor());
        }
    }

    public void addPoints(int newPoints) {
        points = points + newPoints;
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

    public int getYellowAmmo() {
        return yellowAmmo;
    }

    public int getRedAmmo() {
        return redAmmo;
    }

    public PlayerColor getColor() {return color;}

    public boolean isFinalFrenzy() {
        return finalFrenzy;
    }

    public List<WeaponCard> getLoadedWeapons(){
        return new ArrayList<>(this.loadedWeapons);
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

    public Player getClone(){
        try {
            return (Player)this.clone();
        }
        catch(CloneNotSupportedException cNSE){
            cNSE.printStackTrace();
        }
        return null;
    }

    public void setFinalFrenzy(boolean finalFrenzy) {
        this.finalFrenzy = finalFrenzy;
    }
}
