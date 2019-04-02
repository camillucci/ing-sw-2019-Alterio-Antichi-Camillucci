package it.polimi.ingsw.model;

public abstract class Square {
    private Square[] squares;
    private SquareBorder[] borderType;

    public abstract void grab(Player player);
}
