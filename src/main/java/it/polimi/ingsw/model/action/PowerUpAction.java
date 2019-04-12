package it.polimi.ingsw.model.action;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.PowerUpCard;

public class PowerUpAction extends ShootAction
{
    public PowerUpAction()
    {
        this.shootFuncP = (a,b) -> {};
        this.shootFuncS = (a,b) -> {};
    }
    @Override
    public void addPowerUp(PowerUpCard powerUpCard)
    {
        if(Ammo.getAmmo(ownerPlayer).sub(doActionCost).isGreaterOrEqual(powerUpCard.cost))
        {
            this.doActionCost = this.doActionCost.add(powerUpCard.cost);
            this.shootFuncP = this.shootFuncP.andThen(powerUpCard::shootP);
            this.shootFuncS = this.shootFuncS.andThen(powerUpCard::shootS);
            this.possibleTargetsFuncP = powerUpCard::visiblePlayers;
            this.possibleTargetsFuncS = powerUpCard::visibleSquares;
            this.selectedPowerUp.add(powerUpCard);
        }
    }
}
