package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WeaponCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;

public class ReloadSelectionAction extends SelectionAction
{
    public ReloadSelectionAction()
    {
        this.optional = true;
    }

    @Override
    protected void op()
    {
        this.optional = true;
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            SelectionAction wi = new SelectionAction(new Branch(new ReloadAction(wc), new EndBranchAction()), wc);
            w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
