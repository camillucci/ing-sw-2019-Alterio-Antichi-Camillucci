package it.polimi.ingsw.model;

public abstract class Square {
    protected SquareBorder north;
    protected SquareBorder sud;
    protected SquareBorder west;
    protected SquareBorder est;

    public abstract void grab(Player player);
}
