package it.polimi.ingsw.model;

import java.util.ArrayList;

public abstract class Action implements Visualizable
{
    public Event CompletedActionEvent = new Event();
    public Action(Player ownerPlayer)
    {
        this.currentPlayer = ownerPlayer;
    }

    public void doAction()
    {
        this.op();
        CompletedActionEvent.invoke(this, this);
    }
    public abstract void op();

    public void addTarget(Square target)
    {
        this.targetSquares.add(target);
    }
    public void addTarget(Player target)
    {
        this.targetPlayers.add(target);
    }
    public boolean IsCompatible(Action action)
    {
        return action instanceof Action;
    }

    protected Player currentPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();
    protected ArrayList<Player> targetPlayers = new ArrayList<>();

}
