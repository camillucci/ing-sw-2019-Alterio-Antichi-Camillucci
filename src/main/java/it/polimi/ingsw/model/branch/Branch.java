package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Branch
{
    public final Event<Branch, Action> actionCompletedEvent = new Event<>();
    public final Event<Branch, ExtendableAction> extActionCompletedEvent = new Event<>();
    public final Event<Branch, EndBranchAction> endBranchEvent = new Event<>();

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
        for(Action tmp = curAction; tmp.isOptional();) //tmp can't be null because a branch must ends with a non optional action
            ret.add(tmp = getNextAction(tmp));
        return ret;
    }
    public boolean goNext(Action justDoneAction)
    {
        Action nextAction = getNextAction(justDoneAction);

        if(invalidState || nextAction == null){
            setInvalidState();
            return false;
        }
        // actions.size() > 0

        while(!actions.get(0).isCompatible(justDoneAction))
            actions.remove(0);
        actions.remove(0);
        curAction = nextAction;

        return true;
    }
    public boolean isInvalidBranch() {
        return invalidState;
    }
    private void setInvalidState()
    {
        this.invalidState = true;
    }

    private Action getNextAction(Action action)
    {
        for(int i=0; i < actions.size()-1; i++)
            if(actions.get(i).isCompatible(action))
                return actions.get(i+1);
            else if(!actions.get(i).isOptional())
                return null;

        return actions.isEmpty() || !actions.get(actions.size()-1).isCompatible(action) ? null : finalAction;
    }
}
