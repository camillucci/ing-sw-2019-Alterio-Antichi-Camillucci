package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.TargetsFilters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * This class represents a specific case of action. It contains the methods and info relative to an action used to
 * apply the effects of a counter power up card.
 */
public class CounterPowerUpAction extends PowerUpAction
{
    /**
     * Constructor that subscribes to the completedAction event.
     */
    public CounterPowerUpAction()
    {
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.getPowerupDeck().addDiscarded(selectedPowerUp);
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            }
        });
        this.visualizable = new Visualizable("use a Tagback Grenade", "powerup");
    }

    @Override
    protected void preparePowerUp() {
        CounterPowerUpAction tmp = new CounterPowerUpAction();
        tmp.setTargets(damagedPlayers);
    }

    /**
     * This method assigns the shoot function of the input power up card to this class's shoot function, then it makes
     * the input power up card the selected one.
     * @param powerUpCard
     */
    @Override
    public void use(PowerUpCard powerUpCard) {
        this.shootFunc = powerUpCard.shootFunc;
        this.selectedPowerUp = powerUpCard;
    }

    public void setTargets(List<Player> targets)
    {
        damagedPlayers = targets;
        this.playersFilter = (shooter, players, squares) -> {
            if(targetPlayers.isEmpty())
                return targets;
            return Collections.emptyList();
        };
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getCounterAttackPUs()) : Collections.emptyList();
    }

    /**
     * Getter that returns a boolean parameter that represents whether the input action is compatible with this action.
     * @param action Action this class is going to be compared to
     * @return A parameter that represents whether the input is compatible with this action.
     */
    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    /**
     * The comparison is based on the fact the input parameter is always a generic PowerUp action.
     * @param action Action this class is going to be compared to
     * @return Always false
     */
    @Override
    public boolean testCompatibilityWith(PowerUpAction action) {
        return false;
    }

    /**
     * The comparison is based on the fact the input parameter is always a CounterPowerUp action.
     * @param action Action this class is going to be compared to
     * @return Always true
     */
    @Override
    public boolean testCompatibilityWith(CounterPowerUpAction action) {
        return true;
    }
}
