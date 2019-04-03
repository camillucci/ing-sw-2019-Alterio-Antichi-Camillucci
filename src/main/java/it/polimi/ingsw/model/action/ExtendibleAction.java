package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtendibleAction extends Action
{
    protected ExtendibleAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }
    public ExtendibleAction(Player ownerPlayer, List<Branch> branches)
    {
        super(ownerPlayer);
        this.branches = new ArrayList<>(branches);
    }

    public List<Branch> GetBranches()
    {
        return new ArrayList<>(this.branches);
    }

    @Override
    public void visualize()
    {
        //Nothing
    }

    protected List<Branch> branches;
}
