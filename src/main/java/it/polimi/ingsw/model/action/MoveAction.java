package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

public class MoveAction extends Action
{
    private int maxDistance;

    public MoveAction(Player ownerPlayer, int maxDistance)
    {
        super(ownerPlayer);
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
    public void visualize() {
        //TODO
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
