package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {

    private String name;
    private int points;
    private int blueAmmo;
    private int yellowAmmo;
    private int redAmmo;
    private int skull;
    private PlayerColor color;
    private ArrayList<PlayerColor> damage;
    private ArrayList<PlayerColor> mark;
    private ArrayList<WeaponCard> weapons;
    private ArrayList<PowerupCard> powerups;
    private ArrayList<PlayerDeathSubscriber> subscribers;
    private GameBoard board;
    private Square currentSquare;

    public Player (String name, PlayerColor color) {

        this.name = name;
        this.points = 0;
        this.blueAmmo = 0;
        this.yellowAmmo = 0;
        this.redAmmo = 0;
        this.skull = 0;
        this.color = color;
        damage = new ArrayList<>();
        mark = new ArrayList<>();
        weapons = new ArrayList<>();
        powerups = new ArrayList<>();
        subscribers = new ArrayList<>(); //need to add a subscriber//
    }

   public void addDeathSubscriber () {
        //TODO//
   }

}
