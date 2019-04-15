package it.polimi.ingsw.model.action;

public class RollBackAction extends Action
{
    @Override
    public boolean isCompatible(Action a)
    {
        return true;
    }
}
