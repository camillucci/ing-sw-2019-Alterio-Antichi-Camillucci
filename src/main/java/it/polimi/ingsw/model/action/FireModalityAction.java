package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    public FireModalityAction(int[] cost, List<Branch> branches)
    {
        super(branches);
        this.blueCost = cost[0];
        this.redCost = cost[1];
        this.yellowCost = cost[2];
    }
    public FireModalityAction(int[] cost, Branch ... branches)
    {
        this(cost, Arrays.asList(branches));
    }
}
