package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.AmmoColor.*;
import static it.polimi.ingsw.model.SquareBorder.*;

public class GameBoard {

    private List<Player> players = new ArrayList<>();
    private WeaponDeck weaponDeck = new WeaponDeck();
    private PowerUpDeck powerupDeck = new PowerUpDeck();
    private AmmoDeck ammoDeck = new AmmoDeck();
    private Square[][] squares = new Square[3][4];
    private int skulls;
    private int gameSize;
    private List<List<PlayerColor>> killShotTrack = new ArrayList<>();

    public GameBoard(int gameLength, int gameSize) {
        this.skulls = gameLength;
        this.gameSize = gameSize;
        for(int i = 0; i < 8 - skulls; i++)
            killShotTrack.add(new ArrayList<>());

        ArrayList<WeaponCard> blueShop = new ArrayList<>();
        ArrayList<WeaponCard> redShop = new ArrayList<>();
        ArrayList<WeaponCard> yellowShop = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            blueShop.add(weaponDeck.draw());
            redShop.add(weaponDeck.draw());
            yellowShop.add(weaponDeck.draw());
        }
        if(gameSize == 10) {
            squares[0][0] = new AmmoSquare(NOTHING, ROOM, NOTHING, ROOM, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(NOTHING, WALL, ROOM, ROOM, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(BLUE, NOTHING, DOOR, ROOM, NOTHING, blueShop);
            squares[0][3] = null;
            squares[1][0] = new SpawnAndShopSquare(RED, DOOR, NOTHING, NOTHING, ROOM, redShop);
            squares[1][1] = new AmmoSquare(WALL, DOOR, ROOM, ROOM, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(DOOR, WALL, ROOM, DOOR, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(NOTHING, ROOM, DOOR, NOTHING, ammoDeck.draw());
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(DOOR, NOTHING, NOTHING, ROOM, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(WALL, NOTHING, ROOM, DOOR, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(YELLOW, ROOM, NOTHING, DOOR, NOTHING, yellowShop);
        }
        else if (gameSize == 11) {
            squares[0][0] = new AmmoSquare(NOTHING, DOOR, NOTHING, ROOM, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(NOTHING, WALL, ROOM, ROOM, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(BLUE, NOTHING, DOOR, ROOM, DOOR, blueShop);
            squares[0][3] = new AmmoSquare(NOTHING, DOOR, DOOR, NOTHING, ammoDeck.draw());
            squares[1][0] = new SpawnAndShopSquare(RED, DOOR, NOTHING, NOTHING, ROOM, redShop);
            squares[1][1] = new AmmoSquare(WALL, DOOR, ROOM, WALL, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(DOOR, ROOM, WALL, ROOM, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(DOOR, ROOM, ROOM, NOTHING, ammoDeck.draw());
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(DOOR, NOTHING, NOTHING, DOOR, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(ROOM, NOTHING, DOOR, ROOM, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(YELLOW, ROOM, NOTHING, ROOM, NOTHING, yellowShop);
        }
        else {
            squares[0][0] = new AmmoSquare(NOTHING, ROOM, NOTHING, DOOR, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(NOTHING, DOOR, DOOR, ROOM, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(BLUE, NOTHING, DOOR, ROOM, DOOR, blueShop);
            squares[0][3] = new AmmoSquare(NOTHING, DOOR, DOOR, NOTHING, ammoDeck.draw());
            squares[1][0] = new SpawnAndShopSquare(RED, ROOM, DOOR, NOTHING, WALL, redShop);
            squares[1][1] = new AmmoSquare(DOOR, DOOR, WALL, WALL, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(DOOR, ROOM, WALL, ROOM, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(DOOR, ROOM, ROOM, NOTHING, ammoDeck.draw());
            squares[2][0] = new AmmoSquare(DOOR, NOTHING, NOTHING, ROOM, ammoDeck.draw());
            squares[2][1] = new AmmoSquare(DOOR, NOTHING, ROOM, DOOR, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(ROOM, NOTHING, DOOR, ROOM, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(YELLOW, ROOM, NOTHING, ROOM, NOTHING, yellowShop);
        }
    }

    public void addKillShotTrack(List<PlayerColor> newKillShot) {
        if(killShotTrack.size() < 8)
            killShotTrack.add(newKillShot);
        else
            killShotTrack.get(7).addAll(newKillShot);
    }

    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public PowerUpDeck getPowerUpDeck() {
        return powerupDeck;
    }

    public List<List<PlayerColor>> getKillShotTrack() {
        return killShotTrack;
    }
}
