package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ExtendibleAction;
import it.polimi.ingsw.model.action.RollBackAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Branch
{
    public final Event<Branch, Action> actionCompletedEvent = new Event<>();
    public final Event<Branch, ExtendibleAction> extActionCompletedEvent = new Event<>();
    public final Event<Branch, EndBranchAction> endBranchEvent = new Event<>();
    public final Event<Branch, RollBackAction> rollbackEvent = new Event<>();
    private ArrayList<Action> actions;
    private Action finalAction;
    private Action curAction;

    private Branch(Action finalAction, List<Action> actions) {
        this.actions = new ArrayList<>(actions);
        this.finalAction = finalAction;
        for (Action ac : this.actions)
            ac.completedActionEvent.addEventHandler((s,a)->this.actionCompletedEvent.invoke(this, a));
        curAction = this.actions.isEmpty() ? this.finalAction : this.actions.get(0);
    }
    public Branch(List<Action> actions, EndBranchAction endBranchAction)
    {
        this(endBranchAction, actions);
        endBranchAction.completedActionEvent.addEventHandler((s, a)->this.endBranchEvent.invoke(this, (EndBranchAction)a));
    }
    public Branch(List<Action> actions, ExtendibleAction extendibleAction)
    {
        this(extendibleAction, actions);
        extendibleAction.completedActionEvent.addEventHandler((s, a)->this.extActionCompletedEvent.invoke(this, (ExtendibleAction)a));
    }
    public Branch(RollBackAction rollBackAction)
    {
        this(rollBackAction, Collections.emptyList());
        rollBackAction.completedActionEvent.addEventHandler((s, a)->this.rollbackEvent.invoke(this, (RollBackAction)a));
    }
    public Branch(Action action, EndBranchAction endBranchAction)
    {
        this(Collections.singletonList(action), endBranchAction);
    }
    public Branch(Action action, ExtendibleAction extendibleAction)
    {
        this(Collections.singletonList(action), extendibleAction);
    }
    public Branch(ExtendibleAction extendibleAction)
    {
        this(Collections.emptyList(), extendibleAction);
    }
    public Action getCurAction()
    {
        return curAction;
    }
    public boolean endOfBranchReached()
    {
        return curAction == null;
    }
    public void goNext()
    {
        if(actions.isEmpty())
            curAction = null;
        this.actions.remove(0);
        curAction = actions.isEmpty() ? finalAction : actions.get(0);
    }
}
