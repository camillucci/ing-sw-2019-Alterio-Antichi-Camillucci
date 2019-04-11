package it.polimi.ingsw.model;

import java.util.List;

public abstract class Square {
    protected int x;
    protected int y;
    protected SquareBorder north;
    protected SquareBorder south;
    protected SquareBorder west;
    protected SquareBorder east;
    protected List<Player> players;

    public abstract void grab(Player player);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
}
