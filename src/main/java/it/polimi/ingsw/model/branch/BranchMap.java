package it.polimi.ingsw.model.branch;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to manage all the current branches of actions the player has access to. It selects the branches
 * that are still legally possible after every action chosen by the player.
 */
public class BranchMap
{
    /**
     * Event other classes can subscribe to (when it is invoked it notifies the subscribers). It is invoked when an
     * action has been completed.
     */
    public final IEvent<BranchMap, List<Action>> newActionsEvent = new Event<>();

    /**
     * Event other classes can subscribe to (when it is invoked it notifies the subscribers). It is invoked when the
     * whole branch map has been completed.
     */
    public final IEvent<BranchMap, Boolean> endOfBranchMapReachedEvent = new Event<>();

    /**
     * Event other classes can subscribe to (when it is invoked it notifies the subscribers). It is invoked when a
     * rollback action is completed.
     */
    public final IEvent<BranchMap, RollBackAction> rollbackEvent = new Event<>();

    /**
     * Boolean that's set to true when either rollbackEvent or endOfBranchMapReachedEvent is called.
     */
    private boolean invalidState = false;

    /**
     * Action present in every branch map (a dedicated branch is created for this action). Its role is to allow the
     * player to fall back to the previous checkpoint.
     */
    private RollBackAction rollBackAction = new RollBackAction();

    /**
     * List of branches of actions that the map is managing
     */
    private List<Branch> branches;

    /**
     * Constructor. It assigns the branches and subscribes to the events.
     * @param branches List of branches of actions that the map is managing
     */
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

    /**
     * Calculates the list of possible actions that the player can select based on their previous choices
     * @return The list of possible actions the player can select based on their previous choices
     */
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

    /**
     * This method is called when an action is completed. It removes the completed action from the branches that
     * had it and generates a newActionsEvent invocation.
     * @param completedAction The action that has been finished
     */
    private void onBranchActionCompleted(Action completedAction)
    {
       this.branches.removeIf(b -> !b.goNext(completedAction));
        ((Event<BranchMap, List<Action>>)this.newActionsEvent).invoke(this, this.getPossibleActions());
    }

    /**
     * This method is called when an extendable action is completed. It removes the completed action from the branches
     * that had it and generates a newActionsEvent invocation.
     * @param extendableAction The extendable action that has been finished
     */
    private void onBranchExtActionCompleted(ExtendableAction extendableAction)
    {
        this.branches.removeIf(b -> !b.goNext(extendableAction));
        this.branches.addAll(extendableAction.getBranches());
        setupBranches(branches);
        ((Event<BranchMap, List<Action>>)this.newActionsEvent).invoke(this, this.getPossibleActions());
    }

    /**
     * Assigns the input parameter to the map's branches. Also subscribes to the 3 events those branches are connected
     * to.
     * @param branches List of branches the branch map is going to be made up of
     */
    private void setupBranches(List<Branch> branches)
    {
        this.branches = new ArrayList<>(branches);

        for(Branch b : this.branches) // Setup events
        {
            b.actionCompletedEvent.addEventHandler((senderBranch, completedAction) -> onBranchActionCompleted(completedAction));
            b.extActionCompletedEvent.addEventHandler((senderBranch, extendableAction) -> onBranchExtActionCompleted(extendableAction));
            b.endBranchEvent.addEventHandler((s,eba)->((Event<BranchMap, Boolean>)this.endOfBranchMapReachedEvent).invoke(this, false));
        }
    }
}
