package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a specific case of action. It contains the methods and info relative to a generic action that
 * can be expanded by creating a new branch after its execution
 */
public class ExtendableAction extends Action
{
    /**
     * List of branches created after the completion of the extendable action
     */
    protected List<Branch> branches;

    public ExtendableAction() {}

    /**
     * Constructor that assigns the input parameters to their global correspondences.
     * @param branches
     * @param visualizable
     */
    public ExtendableAction(List<Branch> branches, Visualizable visualizable)
    {
        this.branches = new ArrayList<>(branches);
        this.visualizable = visualizable;
    }

    public List<Branch> getBranches()
    {
        return new ArrayList<>(this.branches);
    }

    /**
     * Getter that returns a boolean parameter that represents whether the input action is compatible with this action.
     * @param action Action this class is going to be compared to
     * @return A parameter that represents whether the input is equal to this action.
     */
    @Override
    public boolean isCompatible(Action action) {
        return action == this;
    }
}
