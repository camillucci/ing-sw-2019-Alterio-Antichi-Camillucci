package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;

    public void visualize() {}

    protected SelectionAction powerUpModality;
    public SelectionAction getPowerUpModality()
    {
        buildFireModality();
        return powerUpModality;
    }
    protected abstract void buildFireModality();
}
