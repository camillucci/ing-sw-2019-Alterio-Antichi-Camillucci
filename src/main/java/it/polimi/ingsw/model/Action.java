package it.polimi.ingsw.model;

import java.util.ArrayList;

public abstract class Action implements Visualizable
{
    public Action(Player ownerPlayer)
    {
        this.currentPlayer = ownerPlayer;
    }

    public void doAction()
    {
        this.op();
        for(ActionCompletedSubscriber x : ActionCompletedSubscribers)
            x.OnActionCompleted(this);
    }
    public abstract void op();

    public void addTarget(Square target)
    {
        this.targetSquares.add(target);
    }
    public void addCompletedActionSubscriber(ActionCompletedSubscriber sub)
    {
        this.ActionCompletedSubscribers.add(sub);
    }
    public boolean IsCompatible(Action action)
    {
        return action instanceof Action;
    }

    protected Player currentPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();

    private ArrayList<ActionCompletedSubscriber> ActionCompletedSubscribers = new ArrayList<>();
}
