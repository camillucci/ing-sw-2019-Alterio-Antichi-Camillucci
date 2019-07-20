package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Visualizable;

public class RollBackAction extends Action
{
    public RollBackAction() {
        this.visualizable = new Visualizable(null, "rollback.png","restart the last move", "Restart");
    }

    @Override
    public boolean isCompatible(Action a)
    {
        return true;
    }
}
