package it.polimi.ingsw.model;

public abstract class Square {
    protected SquareBorder north;
    protected SquareBorder south;
    protected SquareBorder west;
    protected SquareBorder east;

    public abstract void grab(Player player);
}
