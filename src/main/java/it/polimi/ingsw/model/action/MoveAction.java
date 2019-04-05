package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

public class MoveAction extends Action
{
    private int maxDist;

    public MoveAction(Player ownerPlayer, int maxDist)
    {
        super(ownerPlayer);
        this.maxDist = maxDist;
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
    public void visualize() {
        //TODO
    }

    @Override
    public boolean isCompatible(Action action)
    {
        if( !(action instanceof  MoveAction) )
            return false;
        MoveAction ma = (MoveAction)action;
        return ma.maxDist <= this.maxDist;
    }
}
