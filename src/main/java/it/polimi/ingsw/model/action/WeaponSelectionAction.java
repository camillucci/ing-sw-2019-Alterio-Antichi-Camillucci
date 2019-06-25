package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;

public class WeaponSelectionAction extends ExtendableAction
{
    public WeaponSelectionAction() {
        this.visualizable = new Visualizable("use a weapon", "weapon");
    }

    @Override
    protected void op()
    {
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            ExtendableAction wi = new ExtendableAction(wc.getFireModalities(), new Visualizable("use " + wc.name, wc.name));
            w.add(new Branch(wi));
        }
        this.branches = w;
    }

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    protected boolean testCompatibilityWith(WeaponSelectionAction action) {
        return true;
    }
}
