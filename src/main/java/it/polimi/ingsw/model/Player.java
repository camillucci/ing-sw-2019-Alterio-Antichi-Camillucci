package it.polimi.ingsw.model;

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
    private ArrayList<PlayerColor> damage = new ArrayList<>();
    private ArrayList<PlayerColor> mark = new ArrayList<>();
    private ArrayList<WeaponCard> loadedWeapons = new ArrayList<>();
    private ArrayList<WeaponCard> unloadedWeapons = new ArrayList<>();
    private ArrayList<PowerUpCard> powerUps = new ArrayList<>();

    public Player (String name, PlayerColor color, GameBoard gameBoard) {

        this.name = name;
        this.points = 0;
        this.blueAmmo = 1;
        this.yellowAmmo = 1;
        this.redAmmo = 1;
        this.skull = 0;
        this.color = color;
        this.gameBoard = gameBoard;
    }

    public void addBlue(int val){
        blueAmmo = blueAmmo + val;
        if(blueAmmo >= 3) {
            blueAmmo = 3;
        }
    }

    public void addRed(int val){
        redAmmo = redAmmo + val;
        if(redAmmo >= 3) {
            redAmmo = 3;
        }
    }

    public void addYellow(int val){
        yellowAmmo = yellowAmmo + val;
        if(yellowAmmo >= 3) {
            yellowAmmo = 3;
        }
    }

    public void addPowerUpCard() {
        if(powerUps.size() < 3){
            powerUps.add(gameBoard.getPowerUpDeck().draw());
        }
    }

    public void addDamage(Player shooter, int val) {
        for (int i = 0; i < val && damage.size() < 12; i++){
            damage.add(shooter.getColor());
        }

        for(PowerUpCard pu : powerUps){
            if(pu instanceof TagbackGrenade){
                pu.visualize();
            }
        }

        //TODO aggiungere metodo che chiama il mirino del giocatore che ha sparato
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

    public List<WeaponCard> getLoadedWeapons(){
        return new ArrayList<>(this.loadedWeapons);
    }

    public List<PowerUpCard> getPowerUps() {
        return powerUps;
    }

    public List<PlayerColor> getDamage() {
        return damage;
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
}
