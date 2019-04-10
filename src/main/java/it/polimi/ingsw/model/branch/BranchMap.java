package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchMap
{
    public final Event<BranchMap, List<Action>> newActionsEvent = new Event<>();
    public final Event<BranchMap, EndBranchAction> endOfBranchMapReachedEvent = new Event<>();
    public final Event<BranchMap, RollBackAction> rollbackEvent = new Event<>();
    private List<Branch> branches;

    protected BranchMap()
    {
    }

    protected BranchMap(List<Branch> branches)
    {
        this.setupBranches(branches);
    }

    public List<Action> getPossibleActions()
    {
        ArrayList<Action> ret = new ArrayList<>();
        for(Branch b: this.branches)
            ret.addAll(b.getCompatibleActions());
        return ret;
    }
    private void onBranchActionCompleted(Branch senderBranch, Action completedAction)
    {
       this.branches.removeIf(b-> !b.goNext(completedAction));
       this.newActionsEvent.invoke(this, this.getPossibleActions());
    }

    private void onBranchExtActionCompleted(Branch senderBranch, ExtendableAction extendableAction)
    {
        this.branches.remove(senderBranch);
        this.branches.addAll(extendableAction.getBranches());
        this.newActionsEvent.invoke(this, getPossibleActions());
    }

    protected void setupBranches(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        RollBackAction rollBackAction = new RollBackAction();
        rollBackAction.completedActionEvent.addEventHandler((s,a)->this.rollbackEvent.invoke(this, (RollBackAction)a));
        this.branches.add(new Branch(rollBackAction)); // Adding Rollback

        for(Branch b : this.branches) // Setup events
        {
            b.actionCompletedEvent.addEventHandler(this::onBranchActionCompleted);
            b.extActionCompletedEvent.addEventHandler(this::onBranchExtActionCompleted);
            b.endBranchEvent.addEventHandler((s,eba)->this.endOfBranchMapReachedEvent.invoke(this, eba));
        }
    }
}
