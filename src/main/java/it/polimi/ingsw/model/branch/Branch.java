package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Branch
{
    public final IEvent<Branch, Action> actionCompletedEvent = new Event<>();
    public final IEvent<Branch, ExtendableAction> extActionCompletedEvent = new Event<>();
    public final IEvent<Branch, EndBranchAction> endBranchEvent = new Event<>();

    private boolean invalidState = false;
    private ArrayList<Action> actions;
    private Action finalAction;

    private Branch(Action finalAction, List<Action> actions) {
        this.actions = new ArrayList<>(actions);
        this.finalAction = finalAction;
        for (Action ac : this.actions)
            ac.completedActionEvent.addEventHandler((s,a)-> ((Event<Branch, Action>)this.actionCompletedEvent).invoke(this, a));
    }

    public Branch(List<Action> actions, EndBranchAction endBranchAction)
    {
        this(endBranchAction, actions);
        endBranchAction.completedActionEvent.addEventHandler((s, a) -> ((Event<Branch, EndBranchAction>)this.endBranchEvent).invoke(this, (EndBranchAction)a));
    }

    public Branch(List<Action> actions, ExtendableAction extendableAction)
    {
        this(extendableAction, actions);
        extendableAction.completedActionEvent.addEventHandler((s, a)->((Event<Branch, ExtendableAction>)this.extActionCompletedEvent).invoke(this, (ExtendableAction)a));
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

    //------------------------------------------------------------------------------------------------------------------

    public List<Action> getCompatibleActions()
    {
        if(invalidState){
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
}
