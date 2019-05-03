package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BranchMap
{
    public final Event<BranchMap, List<Action>> newActionsEvent = new Event<>();
    public final Event<BranchMap, EndBranchAction> endOfBranchMapReachedEvent = new Event<>();
    public final Event<BranchMap, RollBackAction> rollbackEvent = new Event<>();
    private boolean invalidState = false;
    private RollBackAction rollBackAction = new RollBackAction();
    private List<Branch> branches;

    public BranchMap(List<Branch> branches)
    {
        this.setupBranches(branches);
        rollBackAction.completedActionEvent.addEventHandler((s,a)->this.rollbackEvent.invoke(this, (RollBackAction)a));
        rollbackEvent.addEventHandler((a,b)->this.invalidState = true);
        endOfBranchMapReachedEvent.addEventHandler((a,b)->this.invalidState=true);
    }

    public BranchMap(Branch ... branches) {this(Arrays.asList(branches));}

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

    private void onBranchActionCompleted(Branch senderBranch, Action completedAction)
    {
       this.branches.removeIf(b -> !b.goNext(completedAction));
       this.newActionsEvent.invoke(this, this.getPossibleActions());
    }

    private void onBranchExtActionCompleted(Branch senderBranch, ExtendableAction extendableAction)
    {
        onBranchActionCompleted(senderBranch, extendableAction);
        this.branches.addAll(extendableAction.getBranches());
        setupBranches(branches);
    }

    protected void setupBranches(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);

        for(Branch b : this.branches) // Setup events
        {
            b.actionCompletedEvent.addEventHandler(this::onBranchActionCompleted);
            b.extActionCompletedEvent.addEventHandler(this::onBranchExtActionCompleted);
            b.endBranchEvent.addEventHandler((s,eba)->this.endOfBranchMapReachedEvent.invoke(this, eba));
        }
    }
}
