package it.polimi.ingsw.model;

public abstract class Square {
    SquareBorder nord;
    SquareBorder sud;
    SquareBorder west;
    SquareBorder est;

    public abstract void grab(Player player);
}
