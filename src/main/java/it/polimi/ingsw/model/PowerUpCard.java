package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;

    public void visualize() {}

    protected FireModalityAction powerUpModality;
    public FireModalityAction getPowerUpModality()
    {
        buildFireModality();
        return powerUpModality;
    }
    protected abstract void buildFireModality();
}
