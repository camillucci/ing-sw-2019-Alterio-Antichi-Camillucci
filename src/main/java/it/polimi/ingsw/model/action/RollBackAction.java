package it.polimi.ingsw.model.action;

public class RollBackAction extends Action
{
    public RollBackAction() {
        this.text = "restart the last move";
    }

    @Override
    public boolean isCompatible(Action a)
    {
        return true;
    }
}
