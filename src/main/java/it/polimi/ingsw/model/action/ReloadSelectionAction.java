package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WeaponCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;

public class ReloadSelectionAction extends SelectionAction
{
    public ReloadSelectionAction(Player ownerPlayer)
    {
        super(ownerPlayer);
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            SelectionAction wi = new SelectionAction(ownerPlayer, new Branch(new ReloadAction(ownerPlayer, wc), new EndBranchAction(ownerPlayer)), wc);
            w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
