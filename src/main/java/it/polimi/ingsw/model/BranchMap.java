package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class BranchMap
{
    public final Event actionsChangedEvent = new Event();
    public final Event branchMapEmptyEvent = new Event();
    public final Event rollbackEvent = new Event();
    public BranchMap(Player ownerPlayer, List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        this.branches.add(new Branch(this.rollBackAction = new ExtAction(ownerPlayer)));
        for(Branch b : this.branches)
            b.BranchActionCompletedEvent.addEventHandler(this::onBranchActionCompleted);
    }

    public void onBranchActionCompleted(Object sender, Object args)
    {
        Action completedAction = (Action)args;
        if(completedAction == this.rollBackAction)
            rollbackEvent.invoke(this, null);
        else
        {
            RemoveIncompatibleBranches(completedAction);
            notifyChanges();
        }
    }
    private void notifyChanges() {
        if (this.branches.size() == 1)
            branchMapEmptyEvent.invoke(this, null);
        else {
            ArrayList<Action> compatibleActions = new ArrayList<>();
            for (Branch b : this.branches)
                compatibleActions.add(b.getCurAction());
            this.actionsChangedEvent.invoke(this, compatibleActions);
        }
    }

    private void RemoveIncompatibleBranches(Action curAction)
    {
        for (Branch b : branches)
            if (!b.getCurAction().IsCompatible(curAction))
                this.branches.remove(b);
            else if (!b.goNext()) {
                this.branches.addAll(b.getEndAction().GetBranches()); //Il for each beccherà i nuovi aggiunti?
                this.branches.remove(b);
            }
    }
    private List<Branch> branches;
    private ExtAction rollBackAction;
}
