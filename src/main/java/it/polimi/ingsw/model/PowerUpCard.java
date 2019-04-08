package it.polimi.ingsw.model;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;

    public abstract String getName();

    public abstract void visualize();
}
