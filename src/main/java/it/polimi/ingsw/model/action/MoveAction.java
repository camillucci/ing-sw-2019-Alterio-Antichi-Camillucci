package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.List;

public class MoveAction extends Action
{
    private int minDistance;
    private int maxDistance;

    public MoveAction(int maxDistance)
    {
        this.minDistance = -1;
        this.maxDistance = maxDistance;
        this.optional = true;
    }

    public MoveAction(int minDistance, int maxDistance)
    {
        this.minDistance = minDistance;
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
        ownerPlayer.getCurrentSquare().removePlayer(ownerPlayer);
        ownerPlayer.setCurrentSquare(targetSquares.get(0));
        ownerPlayer.getCurrentSquare().addPlayer(ownerPlayer);
    }

    @Override
    public List<Square> getPossibleSquares() {
        if(minDistance == -1)
            return ownerPlayer.gameBoard.getSquares(ownerPlayer, this.maxDistance);
        List<Square> temp = new ArrayList<>(ownerPlayer.gameBoard.getSquares(ownerPlayer, this.maxDistance));
        temp.removeAll(ownerPlayer.gameBoard.getSquares(ownerPlayer, this.minDistance - 1));
        return temp;
    }

    @Override
    public boolean isCompatible(Action action)
    {
        if( !(action instanceof  MoveAction) )
            return false;
        MoveAction ma = (MoveAction)action;
        if(ma.minDistance == -1 || this.minDistance == -1)
            return ma.maxDistance <= this.maxDistance;
        return ma.minDistance >= this.minDistance && ma.maxDistance <= this.maxDistance;
    }
}
