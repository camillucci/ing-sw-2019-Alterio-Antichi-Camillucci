package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

public class GrabAction extends Action
{
    public GrabAction(Player ownerPlayer)
    {
        super(ownerPlayer);
    }
    @Override
    protected void op()
    {
        this.grab();
    }

    private void grab()
    {
        //TODO
    }

    @Override
    public void visualize() {
        //TODO
    }
}
