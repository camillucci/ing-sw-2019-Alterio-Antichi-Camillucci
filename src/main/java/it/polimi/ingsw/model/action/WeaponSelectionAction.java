package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.WeaponCard;

import java.util.ArrayList;

public class WeaponSelectionAction extends SelectionAction
{
    public WeaponSelectionAction()
    {
        //TODO
    }

    @Override
    protected void op()
    {
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            SelectionAction wi = new SelectionAction(wc.getFireModalities(), wc); //
            w.add(new Branch(wi));
        }
        this.branches = w;
    }
}
