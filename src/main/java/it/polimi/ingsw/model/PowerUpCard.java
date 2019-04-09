package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;
    protected SelectionAction powerupModality;

    public void visualize(){
        //TODO put abstract
    };

    protected abstract void buildFireModality(Player shooter);
}
