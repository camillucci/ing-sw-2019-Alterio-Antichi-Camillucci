package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

public class RollBackAction extends Action
{
    public RollBackAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }

    @Override
    public void visualize() {
        //TODO
    }

    @Override
    public boolean isCompatible(Action a)
    {
        return true;
    }
}
