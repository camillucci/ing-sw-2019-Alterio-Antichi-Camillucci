package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int points;
    private int blueAmmo;
    private int yellowAmmo;
    private int redAmmo;
    private int skull;
    private PlayerColor color;
    private ArrayList<PlayerColor> damage = new ArrayList<>();
    private ArrayList<PlayerColor> mark = new ArrayList<>();
    private ArrayList<WeaponCard> loadedWeapons = new ArrayList<>();
    private ArrayList<WeaponCard> unloadedWeapons = new ArrayList<>();
    private ArrayList<PowerupCard> powerups = new ArrayList<>();
    private ArrayList<PlayerDeathSubscriber> subscribers = new ArrayList<>();
    private GameBoard gameBoard;
    private Square currentSquare;

    public Player (String name, PlayerColor color, GameBoard gameBoard) {

        this.name = name;
        this.points = 0;
        this.blueAmmo = 0;
        this.yellowAmmo = 0;
        this.redAmmo = 0;
        this.skull = 0;
        this.color = color;
        this.gameBoard = gameBoard;
        //need to add a subscriber
    }

    public List<WeaponCard> getWeapons(){
        return new ArrayList<>(this.loadedWeapons);
    }
    public void addDeathSubscriber () {
        //TODO//
    }

    public void addRed(int val){

        redAmmo = redAmmo + val;
        if(redAmmo >= 3) {

            redAmmo = 3;
        }
    }

    public void addBlue(int val){

        blueAmmo = blueAmmo + val;
        if(blueAmmo >= 3) {

            blueAmmo = 3;
        }
    }

    public void addYellow(int val){

        yellowAmmo = yellowAmmo + val;
        if(yellowAmmo >= 3) {

            yellowAmmo = 3;
        }
    }

    public void addPowerUpCard() {
        if(powerups.size() == 3){
            return;
        }

        powerups.add(gameBoard.getPowerupDeck().draw());
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
}
