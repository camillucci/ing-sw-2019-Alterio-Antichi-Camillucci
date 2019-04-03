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
    private ArrayList<PlayerColor> damage = new ArrayList<>();
    private ArrayList<PlayerColor> mark = new ArrayList<>();
    private ArrayList<WeaponCard> weapons = new ArrayList<>();
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
        //need to add a subscriber//
    }

   public void addDeathSubscriber () {
        //TODO//
   }

}
