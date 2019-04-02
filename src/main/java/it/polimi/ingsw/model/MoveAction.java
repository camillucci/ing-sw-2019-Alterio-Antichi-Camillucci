package it.polimi.ingsw.model;

public class MoveAction extends Action
{
    public MoveAction(Player ownerPlayer, int maxDist)
    {
        super(ownerPlayer);
        this.maxDist = maxDist;
    }

    @Override
    public void op()
    {
        this.Move();
    }

    public void Move()
    {
        //TODO
    }

    private int maxDist;

    @Override
    public void visualize() {
        //TODO
    }
}
