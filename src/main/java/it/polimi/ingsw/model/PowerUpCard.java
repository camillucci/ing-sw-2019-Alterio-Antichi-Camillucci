package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;

    public abstract void visualize();

    protected SelectionAction powerupModality;
    public SelectionAction getPowerupModality(Player ownerPlayer)
    {
        buildFireModalities(ownerPlayer);
        return powerupModality;
    }
    protected abstract void buildFireModalities(Player shooter);
}
