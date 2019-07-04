package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    public FireModalityAction(Ammo cost, String name, String description, List<Branch> branches)
    {
        this.doActionCost = cost;
        this.branches = new ArrayList<>(branches);
        this.visualizable = new Visualizable(nameToUrl(name,description), "use the " + description, name);
    }

    public FireModalityAction(Ammo cost, String name, String description, Branch ... branches)
    {
        this(cost, name, description, Arrays.asList(branches));
    }

    private String nameToUrl(String name, String description){
        return "/firemodality/" + name.concat("_").concat(description).replace(" ", "_").concat(".png").toLowerCase();
    }

    public Ammo getCost() {
        return new Ammo(doActionCost);
    }
}
