package it.polimi.ingsw.model.action;

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
