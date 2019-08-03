package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Visualizable;

/**
 * This class represents a specific case of action. It contains all the methods and info relative to an action used to
 * rollback the game to the last valid game state
 */
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
