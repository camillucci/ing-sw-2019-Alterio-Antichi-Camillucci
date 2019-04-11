package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.WeaponCard;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;

public class ReloadAction extends Action
{
    private int yellowTot = 0;
    private int blueTot = 0;
    private int redTot = 0;

    public ReloadAction()
    {
        this.optional = true;
    }

    @Override
    protected void op()
    {
        ArrayList<Branch> w = new ArrayList<>();
        for(WeaponCard wc : this.selectedWeapons)
        {
            //TODO this.ownerPlayer.reload(wc)
        }
    }

    @Override
    public void addWeapon(WeaponCard weapon)
    {
        if(this.ownerPlayer.getYellowAmmo() - yellowTot > weapon.yellowReloadCost)
            if(this.ownerPlayer.getRedAmmo() - redTot > weapon.redReloadCost)
                if(this.ownerPlayer.getBlueAmmo() - blueTot > weapon.blueReloadCost)
                {
                    this.yellowTot += weapon.yellowReloadCost;
                    this.blueTot += weapon.blueReloadCost;
                    this.redTot += weapon.redReloadCost;

                    this.selectedWeapons.add(weapon);
                }

    }
}
