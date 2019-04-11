package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExtendableAction extends Action
{
    public ExtendableAction(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
    }

    protected ExtendableAction()
    {
    }
    public List<Branch> getBranches()
    {
        return new ArrayList<>(this.branches);
    }

    protected List<Branch> branches;
}
