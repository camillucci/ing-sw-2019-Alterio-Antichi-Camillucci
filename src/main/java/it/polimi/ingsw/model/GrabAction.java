package it.polimi.ingsw.model;

public class GrabAction extends Action
{
    public GrabAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }
    @Override
    public void op()
    {
        this.grab();
    }

    public void grab()
    {
        //TODO
    }

    @Override
    public void visualize() {
        //TODO
    }
}
