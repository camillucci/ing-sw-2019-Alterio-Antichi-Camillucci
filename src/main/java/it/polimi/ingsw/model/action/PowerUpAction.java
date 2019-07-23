package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is a specific case of a shoot action class. It contains all the generic methods relative to an action
 * that can be used to apply the effect of a power up card.
 */
public class PowerUpAction extends ShootAction
{
    /**
     * Constructor that prepare the usage of a PowerUpCard.
     */
    public PowerUpAction()
    {
        this.optional = true;
        this.canBeDone = false;
        this.playersFilter = TargetsFilters.noPlayersFilter;
        this.squaresFilter = TargetsFilters.noSquaresFilter;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.removePowerUpCard(selectedPowerUp);
        });
        this.visualizable =  new Visualizable("use a Newton or a Teleporter", "Powerup");
    }

    @Override
    protected void op()
    {
        this.shoot();
        if(selectedPowerUp != null) {
            preparePowerUp();
            ownerPlayer.removePowerUpCard(selectedPowerUp);
        }
    }

    @Override
    public void use(PowerUpCard powerUpCard) {
        if(getPossiblePowerUps().contains(powerUpCard)) {
            this.playersFilter = powerUpCard.playersFilter;
            this.squaresFilter = powerUpCard.squaresFilter;
            this.shootFunc = powerUpCard.shootFunc;
            this.selectedPowerUp = powerUpCard;
        }
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps()
    {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getEndStartPUs()) : Collections.emptyList();
    }

    @Override
    protected void preparePowerUp() {
        List<PowerUpCard> temp = new ArrayList<>(ownerPlayer.getPowerupSet().getEndStartPUs());
        temp.remove(selectedPowerUp);
        if(!temp.isEmpty()) {
            PowerUpAction tmp = new PowerUpAction();
            tmp.initialize(ownerPlayer);
            ((Event<Action, Action>)createdActionEvent).invoke(this, tmp);
            this.next = tmp;
        }
    }

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    /**
     * Returns a boolean that represents whether the input is compatible with this class
     * @param action Input action that's going to be compared with this class
     * @return Always true, given that the input parameter is always required to be a PowerUpAction
     */
    @Override
    protected boolean testCompatibilityWith(PowerUpAction action) {
        return true;
    }
}
