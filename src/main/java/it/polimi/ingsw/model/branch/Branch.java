package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a list of actions that are subsequently legal. It always contains an EndBranch action.
 */
public class Branch
{
    /**
     * Event other classes can subscribe to. It is invoked when a full action is completed.
     */
    public final IEvent<Branch, Action> actionCompletedEvent = new Event<>();
    public final IEvent<Branch, ExtendableAction> extActionCompletedEvent = new Event<>();

    /**
     * Event other classes can subscribe to. It is invoked when the EndBranch action is completed.
     */
    public final IEvent<Branch, EndBranchAction> endBranchEvent = new Event<>();

    /**
     * Boolean that gets the "true" value only if the user's decisions no longer match this branch.
     */
    private boolean invalidState = false;

    /**
     * List of actions the branch is made of.
     */
    private ArrayList<Action> actions;

    /**
     * Last action of the branch
     */
    private Action finalAction;

    /**
     * Constructor. It assigns the input parameters to the global ones. It also sets up the events relative to actions
     * being completed.
     * @param finalAction Last action of the branch.
     * @param actions List of actions the branch is made of.
     */
    private Branch(Action finalAction, List<Action> actions) {
        this.actions = new ArrayList<>(actions);
        this.finalAction = finalAction;
        for (Action ac : this.actions) {
            ac.completedActionEvent.addEventHandler((s,a)-> ((Event<Branch, Action>)this.actionCompletedEvent).invoke(this, a));
            ac.createdActionEvent.addEventHandler((lastAction, newAction) -> initializeNewAction(newAction));
        }
    }

    /**
     * Constructor. It assigns the input parameter and subscribes to the completedActionEvent of the EndBranch action,
     * setting up the endBranchEvent when it is invoked.
     * @param actions List of actions the branch is made of.
     * @param endBranchAction EndBranch action relative to this branch.
     */
    public Branch(List<Action> actions, EndBranchAction endBranchAction)
    {
        this(endBranchAction, actions);
        endBranchAction.completedActionEvent.addEventHandler((s, a) -> ((Event<Branch, EndBranchAction>)this.endBranchEvent).invoke(this, (EndBranchAction)a));
    }

    /**
     * Constructor. It assigns the input parameters and subscribes to the completedActionEvent of the extendableAction,
     * setting up  the extActionCompletedEvent which is invoked when the other is.
     * @param actions
     * @param extendableAction
     */
    public Branch(List<Action> actions, ExtendableAction extendableAction)
    {
        this(extendableAction, actions);
        extendableAction.completedActionEvent.addEventHandler((s, a)->((Event<Branch, ExtendableAction>)this.extActionCompletedEvent).invoke(this, (ExtendableAction)a));
    }

    // Useful Constructors. They are all particular cases of the previously listed ones //

    // EndBranchAction constructors //

    /**
     * More specific constructor.
     * @param action
     * @param endBranchAction
     */
    public Branch(Action action, EndBranchAction endBranchAction)
    {
        this(Collections.singletonList(action), endBranchAction);
    }

    /**
     * More specific contructor that takes 2 actions and puts them into an array and then calls the original
     * constructor.
     * @param action1 First action that's going to be put into the array
     * @param action2 Second action that's going to be put into the array
     * @param endBranchAction EndBranch action relative to this branch.
     */
    public Branch(Action action1, Action action2, EndBranchAction endBranchAction)
    {
        this(Arrays.asList(action1, action2),endBranchAction);
    }

    /**
     * More specific contructor that takes 3 actions and puts them into an array and then calls the original
     * constructor.
     * @param action1 First action that's going to be put into the array
     * @param action2 Second action that's going to be put into the array
     * @param action3 Third action that's going to be put into the array
     * @param endBranchAction EndBranch action relative to this branch.
     */
    public Branch(Action action1, Action action2, Action action3, EndBranchAction endBranchAction)
    {
        this(Arrays.asList(action1, action2, action3),endBranchAction);
    }

    // ExtendableAction constructors

    /**
     * More specific constructor that only requires an extendable action as a parameter. It then calls the original
     * constructor using an empty list referring to the actions.
     * @param extendableAction extendable action relative to this branch
     */
    public Branch(ExtendableAction extendableAction)
    {
        this(Collections.emptyList(), extendableAction);
    }

    /**
     * More specific constructor that calls the original constructor by creating a list made by one action.
     * @param action Action that's going to be put into the singleton list.
     * @param extendableAction extendable action relative to this branch
     */
    public Branch(Action action, ExtendableAction extendableAction)
    {
        this(Collections.singletonList(action), extendableAction);
    }

    /**
     * More specific constructor that calls the original constructor by creating a list made by 2 actions.
     * @param action1 First action that's going to be put in the 2 actions list.
     * @param action2 Second action that's going to be put in the 2 actions list.
     * @param extendableAction extendable action relative to this branch
     */
    public Branch(Action action1, Action action2, ExtendableAction extendableAction)
    {
        this(Arrays.asList(action1, action2),extendableAction);
    }

    /**
     * More specific constructor that calls the original constructor by creating a list made by 3 actions.
     * @param action1 First action that's going to be put in the 2 actions list.
     * @param action2 Second action that's going to be put in the 2 actions list.
     * @param action3 Third action that's going to be put in the 2 actions list.
     * @param extendableAction extendable action relative to this branch
     */
    public Branch(Action action1, Action action2, Action action3, ExtendableAction extendableAction)
    {
        this(Arrays.asList(action1, action2, action3),extendableAction);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Calculates which is the list of actions still legally usable by the player. The list is calculated by first
     * checking whether the branch is still compatible with user's previous decisions. Then for every action, this
     * method checks whether it is optional or not and in affirmative cases it adds the action to the return list.
     * @return The list of actions still legally usable by the user.
     */
    public List<Action> getCompatibleActions()
    {
        if(invalidState)
            return Collections.emptyList();

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

    /**
     * Based on the previously selected action, this method calculates whether there is a possible legal continuation
     * compatible with this branch or not.
     * @param justDoneAction Action previously selected (and completed) by the user
     * @return Boolean that represents whether there ia another action legally possible.
     */
    public boolean goNext(Action justDoneAction)
    {
        if(actions.isEmpty()) {
            setInvalidState();
            return false;
        }

        boolean nextFound = false;
        while(!actions.isEmpty() && !nextFound)
            if(actions.get(0).isCompatible(justDoneAction))
                if(actions.get(0).next() != null)
                {
                    actions.set(0, actions.get(0).next());
                    nextFound = true;
                }
                else
                {
                    actions.remove(0);
                    nextFound = true;
                }
            else if(actions.get(0).isOptional())
                actions.remove(0);
            else
            {
                setInvalidState();
                nextFound = true;
            }
        return nextFound;
    }

    private void initializeNewAction(Action action) {
        action.completedActionEvent.addEventHandler((s,a)-> ((Event<Branch, Action>)this.actionCompletedEvent).invoke(this, a));
        action.createdActionEvent.addEventHandler((lastAction, newAction) -> initializeNewAction(newAction));
    }

    /**
     * Getter fo invalidState
     * @return invalidState
     */
    public boolean isInvalidBranch() {
        return invalidState;
    }

    private void setInvalidState()
    {
        this.invalidState = true;
    }
}
