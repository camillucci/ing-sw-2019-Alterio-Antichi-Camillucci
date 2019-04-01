package it.polimi.ingsw.model;

import java.util.ArrayList;

public class BranchMap implements ActionCompletedSubscriber
{
    public BranchMap(ArrayList<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        for(Branch b : this.branches)
            b.AddActionCompletedSubscriber(this);
    }
    @Override
    public void OnActionCompleted(Action actionCompleted)
    {
        for(Branch b: branches)
            if(!b.GetCurAction().IsCompatible(actionCompleted))
                this.branches.remove(b);
            else
                if(!b.GoNext()){
                    this.branches.addAll(b.GetEndAction().GetBranches()); //Il for each beccherà i nuovi aggiunti?
                    this.branches.remove(b);
                }
        NotifyChanges();
    }
    private void NotifyChanges()
    {
        ArrayList<Action> CompatibleActions = new ArrayList<>();
        for(Branch b : this.branches)
            CompatibleActions.add(b.GetCurAction());
        for(ActionsChangedSubscriber sub : actionsChangedSubscribers)
            sub.onActionsChanged(CompatibleActions);
    }
    public void AddActionsChangedSubscriber(ActionsChangedSubscriber sub)
    {
        this.actionsChangedSubscribers.add(sub);
    }

    private ArrayList<Branch> branches;
    private ArrayList<ActionsChangedSubscriber> actionsChangedSubscribers = new ArrayList<>();
}
