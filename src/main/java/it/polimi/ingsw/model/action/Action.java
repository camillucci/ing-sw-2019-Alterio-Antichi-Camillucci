package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Action implements Visualizable
{
    public final Event<Action, Action> completedActionEvent = new Event<>();
    protected Player ownerPlayer;
    protected ArrayList<Square> targetSquares = new ArrayList<>();
    protected ArrayList<Player> targetPlayers = new ArrayList<>();
    protected boolean optional = false;
    private List<Player> possiblePlayers;
    private List<Square> possibleSquares;

    public Action()
    {

    }
    public void initializeAction(Player ownerPlayer)
    {
        this.ownerPlayer = ownerPlayer;
        this.possiblePlayers = getPossiblePlayers();
        this.possibleSquares = getPossibleSquares();
    }
    public void doAction()
    {
        this.op();
        completedActionEvent.invoke(this, this);
    }

    protected void op() {

    }

    public List<Player> getPossiblePlayers(){return Collections.emptyList();}
    public List<Square> getPossibleSquares(){return Collections.emptyList();}
    public boolean isOptional(){return optional;}
    public void addTarget(Square target)
    {
        if(this.possibleSquares.contains(target))
            this.targetSquares.add(target);
    }
    public void addTarget(Player target)
    {
        if(this.possiblePlayers.contains(target))
            this.targetPlayers.add(target);
    }
    public boolean isCompatible(Action action)
    {
        return action.getClass().isInstance(this);
    }
}
