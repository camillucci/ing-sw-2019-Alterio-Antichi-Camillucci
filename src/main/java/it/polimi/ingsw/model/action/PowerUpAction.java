package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.List;

public class PowerUpAction extends ShootAction
{
    public PowerUpAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(playersFilter, squaresFilter, shootFunc);
        this.optional = true;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
        });
    }

    public PowerUpAction(SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(shootFunc, squaresFilter);
        this.optional = true;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
        });
    }

    public PowerUpAction()
    {
        this.optional = true;
        this.playersFilter = TargetsFilters.noPlayersFilter;
        this.squaresFilter = TargetsFilters.noSquaresFilter;
    }

    @Override
    protected void op()
    {
        this.shoot();
        if(selectedPowerUp != null) {
            preparePowerUp();
            targetPlayers.clear();
        }
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
