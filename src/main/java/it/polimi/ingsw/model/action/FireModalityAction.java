package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.branch.Branch;

import java.util.Arrays;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    public FireModalityAction(Ammo cost, List<Branch> branches)
    {
        super(branches);
        this.doActionCost = cost;
    }
    public FireModalityAction(Ammo cost, Branch ... branches)
    {
        this(cost, Arrays.asList(branches));
    }
}
