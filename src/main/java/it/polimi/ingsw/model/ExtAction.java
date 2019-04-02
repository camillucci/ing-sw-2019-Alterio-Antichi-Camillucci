package it.polimi.ingsw.model;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class ExtAction extends Action
{
    public ExtAction(Player ownerPlayer)
    {
        super(ownerPlayer);
        this.branches = new ArrayList<>();
    }
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
        // Nothing
    }

    @Override
    public void visualize()
    {
        //Nothing to visualize
    }

    private List<Branch> branches;
}
