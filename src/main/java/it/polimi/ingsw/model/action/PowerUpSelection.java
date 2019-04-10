package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;

public class PowerUpSelection extends SelectionAction
{
    public PowerUpSelection()
    {

    }

    @Override
    protected void op()
    {
        ArrayList<Branch> w = new ArrayList<>();
        for(PowerUpCard wc : this.ownerPlayer.getPowerUps())
        {
            SelectionAction wi = wc.getPowerUpModality(); //
            w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
