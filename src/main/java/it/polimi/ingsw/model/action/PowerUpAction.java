package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.PowerUpCard;

public class PowerUpAction extends ShootAction
{
    @Override
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(Ammo.getAmmo(ownerPlayer).sub(doActionCost).isGreaterOrEqual(powerUpCard.getCost()))
        {
            this.doActionCost = this.doActionCost.add(powerUpCard.getCost());
            this.playersFilter = powerUpCard.playersFilter;
            this.squaresFilter = powerUpCard.squaresFilter;
            this.selectedPowerUps.add(powerUpCard);
        }
    }

    @Override
    public void shoot()
    {
        for(PowerUpCard pu : this.selectedPowerUps)
            pu.shootFunc.accept(ownerPlayer, targetPlayers, targetSquares);

    }
}
