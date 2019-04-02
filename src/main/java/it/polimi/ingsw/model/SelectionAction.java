package it.polimi.ingsw.model;

import java.util.List;

public class SelectionAction extends ExtAction
{
    public SelectionAction(Player ownerPLayer, List<Branch> modalityBranches, Visualizable visualizable)
    {
        super(ownerPLayer, modalityBranches);
        this.visualizable = visualizable;
    }
    @Override
    public void op()
    {
        //Nothing
    }

    @Override
    public void visualize()
    {
        this.visualizable.visualize();
    }

    private Visualizable visualizable;
}
