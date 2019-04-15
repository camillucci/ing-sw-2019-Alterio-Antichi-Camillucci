package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.WeaponCard;

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
        {
            this.ownerPlayer.reloadWeapon(wc);
        }
    }

    @Override
    public void addWeapon(WeaponCard weapon)
    {
        if(Ammo.getAmmo(ownerPlayer).sub(doActionCost).isGreaterOrEqual(weapon.reloadCost))
        {
            this.doActionCost = this.doActionCost.add(weapon.reloadCost);
            this.selectedWeapons.add(weapon);
        }
    }
}
