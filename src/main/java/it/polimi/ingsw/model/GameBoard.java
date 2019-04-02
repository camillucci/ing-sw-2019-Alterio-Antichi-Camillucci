package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<Player> players;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    private AmmoDeck ammoDeck;
    private List<Square> squares; // Problem: id square and how many
    private int skull; // Problem: how many from 5 to 8
    private int gameLenght;
    private int gameSize;

    public GameBoard(List<Player> players, int gameLength, int gameSize) {
        this.players = players;
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoDeck = new AmmoDeck();
        this.squares = new ArrayList<>(); // Problem: how to add squares
        this.skull = 8;
        this.gameLenght = gameLength;
        this.gameSize = gameSize;
    }
}
