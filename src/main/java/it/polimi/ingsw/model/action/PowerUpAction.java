package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.PowerUpCard;

public class PowerUpAction extends ShootAction
{
    @Override
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(Ammo.getAmmo(ownerPlayer).sub(doActionCost).isGreaterOrEqual(powerUpCard.cost))
        {
            this.doActionCost = this.doActionCost.add(powerUpCard.cost);
            this.possibleTargetsFuncP = powerUpCard::visiblePlayers;
            this.possibleTargetsFuncS = powerUpCard::visibleSquares;
            this.selectedPowerUps.add(powerUpCard);
        }
    }

    @Override
    public void shoot()
    {
        for(PowerUpCard pu : this.selectedPowerUps)
        {
            pu.shootP(this.ownerPlayer, this.targetPlayers);
            pu.shootS(this.ownerPlayer, this.targetSquares);
        }

    }
}
