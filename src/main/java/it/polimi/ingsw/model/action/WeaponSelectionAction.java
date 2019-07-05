package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a specific case of an extendable action class. It contains all the methods relative to an action
 * that can be used to let the player select a weapon they want to use.
 */
public class WeaponSelectionAction extends ExtendableAction
{
    public WeaponSelectionAction() {
        this.visualizable = new Visualizable(null,"gun.png", "use a weapon", "weapon");
    }

    @Override
    protected void op()
    {
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : ownerPlayer.getLoadedWeapons())
        {
            ExtendableAction wi = new ExtendableAction(wc.getFireModalities(ownerPlayer, discardedPowerUps), new Visualizable(nameToUrl(wc.name), "use " + wc.name, "select a weapon"));
            w.add(new Branch(wi));
        }
        this.branches = w;
    }

    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        List<PowerUpCard> temp = new ArrayList<>();
        Ammo alreadyAdded = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            alreadyAdded = alreadyAdded.add(pu.colorToAmmo());
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            for(WeaponCard weaponCard : ownerPlayer.getLoadedWeapons())
                for(FireModalityAction fireModalityAction : weaponCard.fireBuilder.get())
                    if(pu.colorToAmmo().add(alreadyAdded).isLessOrEqualThan(fireModalityAction.getCost()) && !temp.contains(pu) && !discardedPowerUps.contains(pu)) {
                        temp.add(pu);
                        break;
                    }
        return temp;
    }

    private String nameToUrl(String name){
        return "/weapon/" + name.replace(" ", "_").concat(".png").toLowerCase();
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
