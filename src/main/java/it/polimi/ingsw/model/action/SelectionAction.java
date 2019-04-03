package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;

import java.util.List;

public class SelectionAction extends ExtendibleAction
{
    protected SelectionAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }

    public SelectionAction(Player ownerPLayer, List<Branch> selectionBranches, Visualizable visualizable)
    {
        super(ownerPLayer, selectionBranches);
        this.visualizable = visualizable;
    }

    @Override
    public void visualize()
    {
        this.visualizable.visualize();
    }

    @Override
    public boolean isCompatible(Action action)
    {
        return action == this;
    }
    private Visualizable visualizable;
}
