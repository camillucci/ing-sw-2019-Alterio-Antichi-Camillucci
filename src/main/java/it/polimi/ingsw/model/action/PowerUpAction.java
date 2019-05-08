package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.PlayersFilter;
import it.polimi.ingsw.model.weapons.ShootFunc;
import it.polimi.ingsw.model.weapons.SquaresFilter;

import java.util.ArrayList;
import java.util.List;

public class PowerUpAction extends ShootAction
{
    public PowerUpAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(playersFilter,squaresFilter, shootFunc);
        this.optional = true;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
        });
    }
    public PowerUpAction()
    {
        this.optional = true;
    }

    @Override
    protected void op()
    {
        this.shoot();
        if(selectedPowerUp != null)
            preparePowerUp();
    }

    @Override
    protected void preparePowerUp() {
        this.next = selectedPowerUp.getEffect();
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps()
    {
        return new ArrayList<>(ownerPlayer.getPowerupSet().getEndStartPUs());
    }
}
