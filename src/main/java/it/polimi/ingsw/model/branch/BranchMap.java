package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BranchMap
{
    public final IEvent<BranchMap, List<Action>> newActionsEvent = new Event<>();
    public final IEvent<BranchMap, EndBranchAction> endOfBranchMapReachedEvent = new Event<>();
    public final IEvent<BranchMap, RollBackAction> rollbackEvent = new Event<>();
    private boolean invalidState = false;
    private RollBackAction rollBackAction = new RollBackAction();
    private List<Branch> branches;

    public BranchMap(List<Branch> branches)
    {
        this.setupBranches(branches);
        rollBackAction.completedActionEvent.addEventHandler((s,a)-> ((Event<BranchMap, RollBackAction>)this.rollbackEvent).invoke(this, (RollBackAction)a));
        rollbackEvent.addEventHandler((a,b)->this.invalidState = true);
        endOfBranchMapReachedEvent.addEventHandler((a,b)->this.invalidState=true);
    }

    public BranchMap(Branch ... branches) {
        this(Arrays.asList(branches));
    }

    public List<Action> getPossibleActions()
    {
        if(invalidState)
            return Collections.emptyList();

        List<Action> ret = new ArrayList<>();
        for(Branch b: this.branches)
            for(Action a : b.getCompatibleActions())
                if(ret.stream().noneMatch((a2 -> a2.isCompatible(a) && a.isCompatible(a2))))
                    ret.add(a);
        ret.add(rollBackAction);
        return ret;
    }

    private void onBranchActionCompleted(Action completedAction)
    {
       this.branches.removeIf(b -> !b.goNext(completedAction));
        ((Event)this.newActionsEvent).invoke(this, this.getPossibleActions());
    }

    private void onBranchExtActionCompleted(ExtendableAction extendableAction)
    {
        this.branches.removeIf(b -> !b.goNext(extendableAction));
        this.branches.addAll(extendableAction.getBranches());
        setupBranches(branches);
        ((Event<BranchMap, List<Action>>)this.newActionsEvent).invoke(this, this.getPossibleActions());
    }

    protected void setupBranches(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);

        for(Branch b : this.branches) // Setup events
        {
            b.actionCompletedEvent.addEventHandler((senderBranch, completedAction) -> onBranchActionCompleted(completedAction));
            b.extActionCompletedEvent.addEventHandler((senderBranch, extendableAction) -> onBranchExtActionCompleted(extendableAction));
            b.endBranchEvent.addEventHandler((s,eba)->((Event<BranchMap, EndBranchAction>)this.endOfBranchMapReachedEvent).invoke(this, eba));
        }
    }
}
