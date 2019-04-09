package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.WeaponCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;

public class PowerUpSelection extends SelectionAction
{
    public PowerUpSelection(Player ownerPlayer)
    {
        super(ownerPlayer);
        ArrayList<Branch> w = new ArrayList<>();
        for(PowerUpCard wc : ownerPlayer.getPowerUps())
        {
            //SelectionAction wi = new SelectionAction(ownerPlayer, wc.getFireModalities(ownerPlayer), wc); //
            //w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
