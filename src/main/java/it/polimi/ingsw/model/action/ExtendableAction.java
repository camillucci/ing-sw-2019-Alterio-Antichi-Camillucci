package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtendableAction extends Action
{
    protected List<Branch> branches;

    public ExtendableAction(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
    }

    public ExtendableAction(Branch ... branches){ this(Arrays.asList(branches)); }

    public List<Branch> getBranches()
    {
        return new ArrayList<>(this.branches);
    }

    @Override
    public boolean isCompatible(Action action) {
        return action == this;
    }
}
