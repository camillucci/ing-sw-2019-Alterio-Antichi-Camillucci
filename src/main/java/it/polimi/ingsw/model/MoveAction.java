package it.polimi.ingsw.model;

public class MoveAction extends Action
{
    public MoveAction(Player ownerPlayer, int MaxDist)
    {
        super(ownerPlayer);
        this.MaxDist = MaxDist;
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

    private int MaxDist;

    @Override
    public void visualize() {
        //TODO
    }
}
