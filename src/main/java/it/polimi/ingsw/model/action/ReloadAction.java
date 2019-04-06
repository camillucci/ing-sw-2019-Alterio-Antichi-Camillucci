package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.WeaponCard;

public class ReloadAction extends Action
{
    @Override
    public void op()
    {
        this.reload();
    }

    private void reload()
    {
        // TODO
        // this.player.RemoveAmmo(this.weapon.ammo)
        // this.weapon.SetReload(true)
    }
    public ReloadAction(Player ownerPlayer, WeaponCard weapon)
    {
        super(ownerPlayer);
        this.weapon = weapon;
    }

    private WeaponCard weapon;

    @Override
    public void visualize() {
        //TODO
    }
}