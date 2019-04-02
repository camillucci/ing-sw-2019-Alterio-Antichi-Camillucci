package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class BranchMap implements ActionCompletedSubscriber
{
    public BranchMap(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        for(Branch b : this.branches)
            b.addActionCompletedSubscriber(this);
    }
    @Override
    public void onActionCompleted(Action actionCompleted)
    {
        for(Branch b: branches)
            if(!b.getCurAction().IsCompatible(actionCompleted))
                this.branches.remove(b);
            else
                if(!b.goNext()){
                    this.branches.addAll(b.getEndAction().GetBranches()); //Il for each beccherà i nuovi aggiunti?
                    this.branches.remove(b);
                }
        notifyChanges();
    }
    private void notifyChanges()
    {
        if(branches.isEmpty())
            /*Notify  Turn*/;
        ArrayList<Action> compatibleActions = new ArrayList<>();
        for(Branch b : this.branches)
            compatibleActions.add(b.getCurAction());
        for(ActionsChangedSubscriber sub : actionsChangedSubscribers)
            sub.onActionsChanged(compatibleActions);
    }
    public void AddActionsChangedSubscriber(ActionsChangedSubscriber sub)
    {
        this.actionsChangedSubscribers.add(sub);
    }

    private List<Branch> branches;
    private List<ActionsChangedSubscriber> actionsChangedSubscribers = new ArrayList<>();
}
