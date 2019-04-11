package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FireModalityAction extends ExtendableAction
{
    private FireModalityAction(List<Branch> branches, int[] cost)
    {
        super(branches);
        this.blueCost = cost[0];
        this.redCost = cost[1];
        this.yellowCost = cost[2];
    }
    public FireModalityAction(Branch branch, int[] cost)
    {
        this(Collections.singletonList(branch), cost);
    }
    public FireModalityAction(Branch branch1, Branch branch2, int[] cost)
    {
        this(Arrays.asList(branch1,branch2), cost);
    }
    public FireModalityAction(Branch branch1, Branch branch2, Branch branch3, int[] cost)
    {
        this(Arrays.asList(branch1,branch2, branch3), cost);
    }
}
