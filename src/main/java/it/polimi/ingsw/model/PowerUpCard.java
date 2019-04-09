package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public abstract class PowerUpCard {

    protected String name;
    protected AmmoColor color;

    public abstract void visualize();

    protected List<SelectionAction> powerupModalities = new ArrayList<>();

    public List<Branch> getPowerUpModalities(Player shooter)
    {
        buildFireModalities(shooter);
        ArrayList<Branch> ret = new ArrayList<>();
        for(SelectionAction a : powerupModalities)
            ret.add(new Branch(a));
        return ret;
    }

    protected abstract void buildFireModalities(Player shooter);
}
