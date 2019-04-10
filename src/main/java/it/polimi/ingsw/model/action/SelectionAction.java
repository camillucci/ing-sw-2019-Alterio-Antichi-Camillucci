package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Visualizable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectionAction extends ExtendableAction
{
    protected SelectionAction()
    {
    }

    public SelectionAction(List<Branch> selectionBranches, Visualizable visualizable)
    {
        super(selectionBranches);
        this.visualizable = visualizable;
    }
    public SelectionAction(Branch branch, Visualizable visualizable)
    {
        this(Collections.singletonList(branch), visualizable);
    }
    public SelectionAction(Branch branch1, Branch branch2, Visualizable visualizable)
    {
        this(Arrays.asList(branch1,branch2), visualizable);
    }
    public SelectionAction(Branch branch1, Branch branch2, Branch branch3, Visualizable visualizable)
    {
        this(Arrays.asList(branch1,branch2, branch3), visualizable);
    }

    @Override
    public void visualize()
    {
        this.visualizable.visualize();
    }

    private Visualizable visualizable;
}
