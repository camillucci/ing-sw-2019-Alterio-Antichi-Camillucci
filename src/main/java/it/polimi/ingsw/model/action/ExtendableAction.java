package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class ExtendableAction extends Action
{
    protected List<Branch> branches;

    public ExtendableAction() {}

    public ExtendableAction(List<Branch> branches, String text)
    {
        this.branches = new ArrayList<>(branches);
        this.text = text;
    }

    public List<Branch> getBranches()
    {
        return new ArrayList<>(this.branches);
    }

    @Override
    public boolean isCompatible(Action action) {
        return action == this;
    }
}
