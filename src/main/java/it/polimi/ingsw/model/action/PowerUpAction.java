package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        this.visualizable = new Visualizable("use a Newton or a Teleporter", "powerup");
    }

    public PowerUpAction(SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this(TargetsFilters.noPlayersFilter, squaresFilter, shootFunc);
    }

    public PowerUpAction()
    {
        this.optional = true;
        this.playersFilter = TargetsFilters.noPlayersFilter;
        this.squaresFilter = TargetsFilters.noSquaresFilter;
        this.visualizable =  new Visualizable("use a Newton or a Teleporter", "powerup");
        this.canBeDone = false;
    }

    @Override
    protected void op()
    {
        this.shoot();
        if(selectedPowerUp != null) {
            preparePowerUp();
            ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            ownerPlayer.gameBoard.getPowerupDeck().addDiscarded(selectedPowerUp);
        }
    }

    @Override
    protected void preparePowerUp() {
        this.next = new PowerUpAction();
    }

    @Override
    public void use(PowerUpCard powerUpCard) {
        this.playersFilter = powerUpCard.playersFilter;
        this.squaresFilter = powerUpCard.squaresFilter;
        this.shootFunc = powerUpCard.shootFunc;
        this.selectedPowerUp = powerUpCard;
    }

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps()
    {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getEndStartPUs()) : Collections.emptyList();
    }

    @Override
    public boolean testCompatibilityWith(PowerUpAction action) {
        return true;
    }
}
