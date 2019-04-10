package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;

import java.util.ArrayList;

public abstract class Action implements Visualizable
{
    public final Event<Action, Action> completedActionEvent = new Event<>();
    protected Player ownerPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();
    protected ArrayList<Player> targetPlayers = new ArrayList<>();
    protected boolean optional = false;

    public Action()
    {

    }
    public void doAction(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
        this.op();
        completedActionEvent.invoke(this, this);
    }

    protected void op() {
        //TODO
    }
    public boolean isOptional(){return optional;}
    public void addTarget(Square target)
    {
        this.targetSquares.add(target);
    }
    public void addTarget(Player target)
    {
        this.targetPlayers.add(target);
    }
    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }
}
