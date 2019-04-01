package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class ExtAction extends Action
{
    public ExtAction(Player ownerPlayer, List<Branch> branches)
    {
        super(ownerPlayer);
        this.branches = new ArrayList<>(branches);
    }

    public List<Branch> GetBranches()
    {
        return new ArrayList<>(this.branches);
    }
    @Override
    public void op()
    {
        // ExtAction visualize only;
    }

    @Override
    public void visualize()
    {

    }

    private List<Branch> branches;
}
