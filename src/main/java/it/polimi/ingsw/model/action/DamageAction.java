package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class DamageAction extends ShootAction
{
    private int totDamage;
    public DamageAction(Player ownerPlayer, int totDamage)
    {
        super(ownerPlayer);
        this.totDamage = totDamage;
        this.shootFuncP = (a,b)->damagePlayer();
    }

    private void damagePlayer()
    {
        //this.targetPlayers.get(0).setDamage(this.totDamage);
    }
}
