package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;

import java.util.List;

import static it.polimi.ingsw.model.SquareBorder.*;

public abstract class Square {
    protected int y;
    protected int x;
    protected SquareBorder north;
    protected SquareBorder south;
    protected SquareBorder west;
    protected SquareBorder east;
    protected List<Player> players;

    public abstract List<Branch> grab(Player player);

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

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public SquareBorder getNorth() {
        return north;
    }

    public SquareBorder getSouth() {
        return south;
    }

    public SquareBorder getWest() {
        return west;
    }

    public SquareBorder getEast() {
        return east;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return Integer.toString(x) + "-" + Integer.toString(y);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public abstract List<String> getCardsName();

    public void removeWeapon(WeaponCard weaponCard) { }

    public abstract void refill();
}
