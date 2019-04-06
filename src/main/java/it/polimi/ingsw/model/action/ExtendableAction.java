package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtendableAction extends Action
{
    protected ExtendableAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }
    public ExtendableAction(Player ownerPlayer, List<Branch> branches)
    {
        super(ownerPlayer);
        this.branches = new ArrayList<>(branches);
    }

    public List<Branch> getBranches()
    {
        return new ArrayList<>(this.branches);
    }

    @Override
    public void visualize()
    {
        //TODO
    }

    protected List<Branch> branches;
}
