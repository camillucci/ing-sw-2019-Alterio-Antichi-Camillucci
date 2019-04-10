package it.polimi.ingsw.model.action;

public class damageAction extends ShootAction
{
    private static final int totPlayer = 5;
    private int[] totDamage;

    protected damageAction()
    {
        this.shootFuncP = (a,b)->damagePlayer();
    }

    public damageAction(int damageP1)
    {
        this();
        totDamage = new int[] {damageP1};
    }
    public damageAction(int damageP1, int damageP2)
    {
        this();
        totDamage = new int[] {damageP1, damageP2};
    }
    public damageAction(int damageP1, int damageP2, int damageP3)
    {
        this();
        totDamage = new int[] {damageP1, damageP2, damageP3};
    }
    public damageAction(int damageP1, int damageP2, int damageP3, int damageP4)
    {
        this();
        totDamage = new int[] {damageP1, damageP2, damageP3, damageP4};
    }
    public damageAction(int damageP1, int damageP2, int damageP3, int damageP4, int damageP5)
    {
        this();
        totDamage = new int[] {damageP1, damageP2, damageP3, damageP4, damageP5};
    }
    private void damagePlayer()
    {
        for(int i=0; i < totPlayer; i++)
            if(totDamage[i] > 0)
                this.targetPlayers.get(i).addDamage(this.ownerPlayer, totDamage[i]);
    }
}
