package it.polimi.ingsw.model.action;

public class RollBackAction extends Action
{
    @Override
    public boolean isCompatible(Action a)
    {
        //TODO Nope. rollback is persistent. It have to be always compatible, because it could be always triggered
        return true;
    }
}
