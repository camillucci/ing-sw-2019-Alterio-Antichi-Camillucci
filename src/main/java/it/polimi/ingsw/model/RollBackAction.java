package it.polimi.ingsw.model;

public class RollBackAction extends Action
{
    public RollBackAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }
    @Override
    public void op() {

    }

    @Override
    public void visualize() {

    }

    @Override
    public boolean IsCompatible(Action a)
    {
        return true;
    }
}
