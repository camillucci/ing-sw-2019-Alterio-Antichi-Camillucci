package it.polimi.ingsw.model.action;

public class DamageAction extends ShootAction
{
    private int totDamage;
    public DamageAction(int totDamage)
    {
        this.totDamage = totDamage;
        this.shootFuncP = (a,b)->damagePlayer();
    }

    private void damagePlayer()
    {
        //TODO this.targetPlayers.get(0).setDamage(this.totDamage);
    }
}
