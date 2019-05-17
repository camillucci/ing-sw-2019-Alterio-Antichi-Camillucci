package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
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
        Ammo powerUpAmmo = new Ammo(0, 0, 0);
        for(PowerUpCard pu : ownerPlayer.getPowerUps())
            powerUpAmmo = powerUpAmmo.add(pu.colorToAmmo());
        if(ownerPlayer.getAmmo().sub(doActionCost).add(powerUpAmmo).isGreaterOrEqual(weapon.reloadCost))
        {
            this.doActionCost = this.doActionCost.add(weapon.reloadCost);
            this.selectedWeapons.add(weapon);
        }
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
}
