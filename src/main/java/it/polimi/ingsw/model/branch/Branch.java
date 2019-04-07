package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Branch
{
    public final Event<Branch, Action> actionCompletedEvent = new Event<>();
    public final Event<Branch, ExtendableAction> extActionCompletedEvent = new Event<>();
    public final Event<Branch, EndBranchAction> endBranchEvent = new Event<>();
    public final Event<Branch, RollBackAction> rollbackEvent = new Event<>();
    public final Event<Branch, Object> invalidStateEvent = new Event<>();

    private boolean invalidState = false;
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
    public Branch(List<Action> actions, ExtendableAction extendableAction)
    {
        this(extendableAction, actions);
        extendableAction.completedActionEvent.addEventHandler((s, a)->this.extActionCompletedEvent.invoke(this, (ExtendableAction)a));
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
    public Branch(Action action, ExtendableAction extendableAction)
    {
        this(Collections.singletonList(action), extendableAction);
    }
    public Branch(ExtendableAction extendableAction)
    {
        this(Collections.emptyList(), extendableAction);
    }
    public List<Action> getCompatibleActions()
    {
        if(invalidState){
            setInvalidState();
            return Collections.emptyList();
        }

        ArrayList<Action> ret = new ArrayList<>();
        ret.add(this.curAction);
        if(curAction instanceof MoveAction)
            ret.add(getNextAction());
        return ret;
    }
    public void goNext(Action justDoneAction)
    {
        Action nextAction = getNextAction();

        if(invalidState || nextAction == null){
            setInvalidState();
            return;
        }
        // actions.size() > 0

        if(curAction.isCompatible(justDoneAction)){
            curAction = nextAction;
            actions.remove(0);
        }
        else if(curAction instanceof MoveAction && nextAction.isCompatible(justDoneAction)){
            actions.remove(0);
            curAction = getNextAction();
            if(!actions.isEmpty())
                actions.remove(0);
        }
        else
            setInvalidState();
    }
    public boolean isInvalidBranch() {
        return invalidState;
    }
    private void setInvalidState()
    {
        this.invalidState = true;
        invalidStateEvent.invoke(this, null);
    }

    private Action getNextAction()
    {
        if(curAction == finalAction)
            return null;

        // actions.size() > 0

        if(actions.size() == 1)
            return finalAction;
        return actions.get(1);

    }
}
