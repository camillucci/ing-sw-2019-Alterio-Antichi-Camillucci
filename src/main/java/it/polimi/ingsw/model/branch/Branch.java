package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.*;

import java.util.*;

public class Branch
{
    public final Event<Branch, Action> actionCompletedEvent = new Event<>();
    public final Event<Branch, ExtendableAction> extActionCompletedEvent = new Event<>();
    public final Event<Branch, EndBranchAction> endBranchEvent = new Event<>();

    private boolean invalidState = false;
    private ArrayList<Action> actions;
    private Action finalAction;

    private Branch(Action finalAction, List<Action> actions) {
        this.actions = new ArrayList<>(actions);
        this.finalAction = finalAction;
        for (Action ac : this.actions)
            ac.completedActionEvent.addEventHandler((s,a)->this.actionCompletedEvent.invoke(this, a));
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
    // Useful Constructors. They are all particular cases of the previous //

    // EndBranchAction constructors //
    public Branch(Action action, EndBranchAction endBranchAction)
    {
        this(Collections.singletonList(action), endBranchAction);
    }
    public Branch(Action action1, Action action2, EndBranchAction endBranchAction)
    {
        this(Arrays.asList(action1, action2),endBranchAction);
    }
    public Branch(Action action1, Action action2, Action action3, EndBranchAction endBranchAction)
    {
        this(Arrays.asList(action1, action2, action3),endBranchAction);
    }
    public Branch(Action action1, Action action2, Action action3, Action action4, EndBranchAction endBranchAction)
    {
        this(Arrays.asList(action1, action2, action3, action4),endBranchAction);
    }

    // ExtendableAction constructors
    public Branch(ExtendableAction extendableAction)
    {
        this(Collections.emptyList(), extendableAction);
    }
    public Branch(Action action, ExtendableAction extendableAction)
    {
        this(Collections.singletonList(action), extendableAction);
    }
    public Branch(Action action1, Action action2, ExtendableAction extendableAction)
    {
        this(Arrays.asList(action1, action2),extendableAction);
    }
    public Branch(Action action1, Action action2, Action action3, ExtendableAction extendableAction)
    {
        this(Arrays.asList(action1, action2, action3),extendableAction);
    }
    public Branch(Action action1, Action action2, Action action3, Action action4, ExtendableAction extendableAction)
    {
        this(Arrays.asList(action1, action2, action3, action4), extendableAction);
    }

    /*****************************************************************/

    public List<Action> getCompatibleActions()
    {
        if(invalidState){
            setInvalidState();
            return Collections.emptyList();
        }

        ArrayList<Action> ret = new ArrayList<>();
        if(actions.isEmpty()) {
            ret.add(finalAction);
            return ret;
        }

        ret.add(actions.get(0));
        for (int i = 0; i < actions.size()-1; i++)
            if(this.actions.get(i).isOptional())
                ret.add(this.actions.get(i+1));
            else
                return ret;
        if(this.actions.get(this.actions.size()-1).isOptional())
            ret.add(finalAction);
        return ret;
    }

    public Branch appendBefore(Action action)
    {
        List<Action> tmp = new ArrayList<>();
        tmp.add(action);
        tmp.addAll(this.actions);
        return new Branch(this.finalAction, tmp);
    }

    public boolean goNext(Action justDoneAction)
    {
        if(actions.isEmpty()) {
            setInvalidState();
            return false;
        }

        boolean found = false;
        while(!actions.isEmpty() && !found)
            if(actions.get(0).isCompatible(justDoneAction))
                if(actions.get(0).next() != null)
                {
                    actions.set(0, actions.get(0).next());
                    found = true;
                }
                else
                {
                    actions.remove(0);
                    found = true;
                }
            else if(actions.get(0).isOptional())
                actions.remove(0);
            else
            {
                setInvalidState();
                found = true;
            }
        return found;
    }
    public boolean isInvalidBranch() {
        return invalidState;
    }
    private void setInvalidState()
    {
        this.invalidState = true;
    }

    private Action getNextCompatibleAction(Action action)
    {
        if(this.actions.isEmpty())
            return null;

        for(int i=0; i < actions.size()-1; i++)
            if(actions.get(i).isCompatible(action))
                if(actions.get(i).next() == null)
                    return actions.get(i+1);
                else
                    return actions.get(i).next();
            else
                if(!actions.get(i).isOptional())
                    return null;

        return actions.get(actions.size()-1).isCompatible(action) ? finalAction : null;
    }
}
