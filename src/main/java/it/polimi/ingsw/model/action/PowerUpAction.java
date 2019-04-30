package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.*;

public class PowerUpAction extends ShootAction
{

    public PowerUpAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.shootFunc = shootFunc;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
    }

    /*
    public PowerUpAction(ShootFunc shootFunc)
    {
        this(null, null, shootFunc);
    }

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
    */

    @Override
    public void shoot() {
        this.shootFunc.accept(ownerPlayer, targetPlayers, targetSquares);
    }
}
