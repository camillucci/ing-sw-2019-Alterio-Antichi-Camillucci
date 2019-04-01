package it.polimi.ingsw.model;

public abstract class Square {
    private Square[] squares;
    private SquareBorder[] borderType;


    public Square() {
        //TODO
    }

    public abstract void grab(Player player);
}
