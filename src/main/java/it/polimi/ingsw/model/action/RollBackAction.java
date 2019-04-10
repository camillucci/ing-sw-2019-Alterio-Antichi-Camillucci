package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

public class RollBackAction extends Action
{
    public RollBackAction()
    {

    }

    @Override
    public boolean isCompatible(Action a)
    {
        return true;
    }

    @Override
    public void visualize() {
        //TODO
    }
}
