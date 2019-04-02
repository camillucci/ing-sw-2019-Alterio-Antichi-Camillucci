package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private List<Player> players;
    private WeaponDeck weaponDeck;
    private PowerupDeck powerupDeck;
    private AmmoDeck ammoDeck;
    private List<Square> squares = new ArrayList<>(); // Problem: id square and how many
    private int skulls;
    private int gameSize;

    public GameBoard(List<Player> players, int gameLength, int gameSize) {
        this.players = players;
        this.skulls = gameLength;
        this.gameSize = gameSize;
        this.weaponDeck = new WeaponDeck();
        this.powerupDeck = new PowerupDeck();
        this.ammoDeck = new AmmoDeck();
        // Problem: how to add squares
    }
}
