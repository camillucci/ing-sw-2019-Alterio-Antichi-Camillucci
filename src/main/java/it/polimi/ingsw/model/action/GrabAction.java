package it.polimi.ingsw.model.action;

public class GrabAction extends Action
{
    @Override
    protected void op()
    {
        this.grab();
    }
    private void grab()
    {
        this.ownerPlayer.getCurrentSquare().grab(this.ownerPlayer);
    }
}
