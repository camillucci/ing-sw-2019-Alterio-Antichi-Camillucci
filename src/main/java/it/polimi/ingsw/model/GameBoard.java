package it.polimi.ingsw.model;

import java.util.ArrayList;

public class GameBoard {
    private ArrayList<Player> players;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    private AmmoDeck ammoDeck;
    private ArrayList<Square> squares; // Problem: id square and how many
    private int skull; // Problem: how many from 5 to 8

    public GameBoard(ArrayList<Player> players) {
        this.players = players;
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoDeck = new AmmoDeck();
        this.skull = 8;
    }
}
