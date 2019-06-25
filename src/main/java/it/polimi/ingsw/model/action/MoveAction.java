package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Square;
import it.polimi.ingsw.model.Visualizable;

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
        this.canBeDone = false;
        this.visualizable = new Visualizable("move", "move");
    }

    public MoveAction(int minDistance, int maxDistance)
    {
        this(maxDistance);
        this.minDistance = minDistance;
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
    public void addTarget(Square target)
    {
        if(this.getPossibleSquares().contains(target)) {
            this.targetSquares.add(target);
            this.canBeDone = true;
        }
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
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    public boolean testCompatibilityWith(MoveAction action)
    {
        if(action.minDistance == -1 || this.minDistance == -1)
            return this.maxDistance <= action.maxDistance;
        return this.minDistance >= action.minDistance &&  this.maxDistance <= action.maxDistance;
    }
}
