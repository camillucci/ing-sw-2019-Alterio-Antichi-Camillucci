package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WeaponCard;

import java.util.ArrayList;

public class WeaponSelectionAction extends SelectionAction
{
    public WeaponSelectionAction(Player ownerPlayer)
    {
        super(ownerPlayer);
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            SelectionAction wi = new SelectionAction(ownerPlayer, wc.getFireModalities(ownerPlayer), wc); //
            w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
