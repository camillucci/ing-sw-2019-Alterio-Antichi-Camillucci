package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.SquareBorder.*;

public abstract class Square {
    public final String name;
    public final int y;
    public final int x;
    public final SquareBorder north;
    public final SquareBorder south;
    public final SquareBorder west;
    public final SquareBorder east;
    protected List<Player> players;

    protected Square(String name, int y, int x, SquareBorder[] borders) {
        this.name = name;
        this.y = y;
        this.x = x;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
    }

    public abstract List<Branch> grab(Player player, List<PowerUpCard> powerUpCards);

    public boolean okNorth() {
        return this.north == DOOR || this.north == ROOM;
    }

    public boolean okSouth() {
        return this.south == DOOR || this.south == ROOM;
    }

    public boolean okWest() {
        return this.west == DOOR || this.west == ROOM;
    }

    public boolean okEast() {
        return this.east == DOOR || this.east == ROOM;
    }

    public boolean existNorth() {
        return this.north == DOOR || this.north == ROOM || this.north == WALL;
    }

    public boolean existSouth() {
        return this.south == DOOR || this.south == ROOM || this.south == WALL;
    }

    public boolean existWest() {
        return this.west == DOOR || this.west == ROOM || this.west == WALL;
    }

    public boolean existEast() {
        return this.east == DOOR || this.east == ROOM || this.east == WALL;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public abstract List<String> getCardsName();

    public void removeWeapon(WeaponCard weaponCard) { }

    public void addWeapon(WeaponCard weaponCard) { }

    public List<WeaponCard> getWeapons() { return Collections.emptyList(); }

    public abstract void refill();
}
