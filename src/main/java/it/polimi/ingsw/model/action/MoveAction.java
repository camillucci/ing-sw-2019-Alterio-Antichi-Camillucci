package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.Collections;
import java.util.List;

public class MoveAction extends Action
{
    private int maxDistance;

    public MoveAction(int maxDistance)
    {
        this.maxDistance = maxDistance;
        this.optional = true;
    }

    @Override
    protected void op()
    {
        this.move();
    }

    private void move()
    {
        //TODO
    }

    @Override
    public List<Player> getPossiblePlayers() {
        return Collections.emptyList();
    }

    @Override
    public List<Square> getPossibleSquares() {
        return ownerPlayer.getGameBoard().getSquares(ownerPlayer, this.maxDistance);
    }

    @Override
    public boolean isCompatible(Action action)
    {
        if( !(action instanceof  MoveAction) )
            return false;
        MoveAction ma = (MoveAction)action;
        return ma.maxDistance <= this.maxDistance;
    }
}
