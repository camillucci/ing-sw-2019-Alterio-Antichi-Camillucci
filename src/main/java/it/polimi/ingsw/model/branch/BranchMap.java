package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ExtendibleAction;
import it.polimi.ingsw.model.action.RollBackAction;

import java.util.ArrayList;
import java.util.List;

public class BranchMap
{
    public final Event<BranchMap, List<Action>> newActionsEvent = new Event<>();
    public final Event<BranchMap, EndBranchAction> endOfBranchMapReachedEvent = new Event<>();
    public final Event<BranchMap, RollBackAction> rollbackEvent = new Event<>();
    private List<Branch> branches;

    public BranchMap(Player ownerPlayer, List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        this.branches.add(new Branch(new RollBackAction(ownerPlayer)));
        for(Branch b : this.branches)
        {
            b.actionCompletedEvent.addEventHandler(this::onBranchActionCompleted);
            b.extActionCompletedEvent.addEventHandler(this::onBranchExtActionCompleted);
            b.rollbackEvent.addEventHandler((s,ra)->this.rollbackEvent.invoke(this, ra));
            b.endBranchEvent.addEventHandler((s,eba)->this.endOfBranchMapReachedEvent.invoke(this, eba));
        }
    }

    public List<Action> getPossibleActions()
    {
        ArrayList<Action> ret = new ArrayList<>();
        for(Branch b: this.branches)
            if(!b.endOfBranchReached())
                ret.add(b.getCurAction());
        return ret;
    }
    private void onBranchActionCompleted(Branch senderBranch, Action completedAction)
    {
        removeIncompatibleBranches(completedAction);
        this.newActionsEvent.invoke(this, this.getPossibleActions());
    }

    private void onBranchExtActionCompleted(Branch senderBranch, ExtendibleAction extendibleAction)
    {
        this.removeIncompatibleBranches(extendibleAction);
        this.branches.remove(senderBranch);
        this.branches.addAll(extendibleAction.GetBranches());
    }
    private void removeIncompatibleBranches(Action curAction)
    {
        for (Branch b : branches)
            if (!b.getCurAction().isCompatible(curAction))
                this.branches.remove(b);
            else
                b.goNext();
    }
}
