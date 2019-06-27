package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ReloadAction extends Action
{
    public ReloadAction()
    {
        this.optional = true;
        this.canBeDone = false;
        this.visualizable = new Visualizable("reload a weapon", "reload");
    }

    @Override
    protected void op()
    {
        for(WeaponCard wc : this.selectedWeapons)
            this.ownerPlayer.reloadWeapon(wc);
    }

    @Override
    public void addWeapon(WeaponCard weapon)
    {
        if(getPossibleWeapons().contains(weapon)) {
            this.doActionCost = this.doActionCost.add(weapon.reloadCost);
            this.selectedWeapons.add(weapon);
            this.canBeDone = true;
        }
    }

    @Override
    public List<WeaponCard> getPossibleWeapons() {
        Ammo powerUpAmmo = new Ammo(0, 0, 0);
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            powerUpAmmo = powerUpAmmo.add(pu.colorToAmmo());
        for(WeaponCard wc : selectedWeapons)
            powerUpAmmo = powerUpAmmo.sub(wc.reloadCost);
        List<WeaponCard> temp = new ArrayList<>();
        for(WeaponCard weapon : ownerPlayer.getUnloadedWeapons())
            if(ownerPlayer.getAmmo().sub(doActionCost).add(powerUpAmmo).isGreaterOrEqual(weapon.reloadCost) && !selectedWeapons.contains(weapon))
                temp.add(weapon);
        return temp;
    }

    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        LinkedHashSet<PowerUpCard> temp = new LinkedHashSet<>();
        Ammo alreadyAdded = new Ammo(0, 0, 0);
        for(PowerUpCard pu : discardedPowerUps)
            alreadyAdded = alreadyAdded.add(pu.colorToAmmo());
        for(WeaponCard wc : this.selectedWeapons)
            for(PowerUpCard pu : ownerPlayer.getPowerUps())
                if(!discardedPowerUps.contains(pu) && alreadyAdded.add(pu.colorToAmmo()).isLessOrEqualThan(wc.reloadCost))
                    temp.add(pu);
        return new ArrayList<>(temp);
    }

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    protected boolean testCompatibilityWith(ReloadAction action) {
        return true;
    }
}
