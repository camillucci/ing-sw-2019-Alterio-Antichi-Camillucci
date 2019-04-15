package it.polimi.ingsw.model.action;

public class GrabAction extends ExtendableAction
{
    @Override
    protected void op()
    {
        this.grab();
    }

    private void grab()
    {
        this.branches = this.ownerPlayer.getCurrentSquare().grab(this.ownerPlayer);
    }
}
