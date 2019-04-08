package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import java.util.Collections;
import java.util.List;

public class SelectionAction extends ExtendableAction
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
    public SelectionAction(Player ownerPlayer, Branch branch, Visualizable visualizable)
    {
        this(ownerPlayer, Collections.singletonList(branch), visualizable);
    }

    @Override
    public void visualize()
    {
        this.visualizable.visualize();
    }

    private Visualizable visualizable;
}
