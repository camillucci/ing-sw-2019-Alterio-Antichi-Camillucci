package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchMap
{
    public final Event<BranchMap, List<Action>> newActionsEvent = new Event<>();
    public final Event<BranchMap, EndBranchAction> endOfBranchMapReachedEvent = new Event<>();
    public final Event<BranchMap, RollBackAction> rollbackEvent = new Event<>();
    private List<Branch> branches;
    protected Player ownerPlayer;

    protected BranchMap(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
    }

    protected BranchMap(Player ownerPlayer, List<Branch> branches)
    {
        this(ownerPlayer);
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
        for(Branch b : branches)
            b.goNext(completedAction);
        this.newActionsEvent.invoke(this, this.getPossibleActions());
    }

    private void onBranchExtActionCompleted(Branch senderBranch, ExtendableAction extendableAction)
    {
        this.branches.remove(senderBranch);
        this.branches.addAll(extendableAction.getBranches());
    }

    protected void setupBranches(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);
        this.branches.add(new Branch(new RollBackAction(ownerPlayer))); // Adding Rollback
        for(Branch b : this.branches) // Setup events
        {
            b.actionCompletedEvent.addEventHandler(this::onBranchActionCompleted);
            b.extActionCompletedEvent.addEventHandler(this::onBranchExtActionCompleted);
            b.rollbackEvent.addEventHandler((s,ra)->this.rollbackEvent.invoke(this, ra));
            b.endBranchEvent.addEventHandler((s,eba)->this.endOfBranchMapReachedEvent.invoke(this, eba));
            b.invalidStateEvent.addEventHandler((s, o)->this.branches.remove(s));
        }
    }
}
