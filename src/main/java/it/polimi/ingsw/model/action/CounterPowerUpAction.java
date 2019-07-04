package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        super();
        this.visualizable = new Visualizable("use a Tagback Grenade", "powerup");
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getCounterAttackPUs()) : Collections.emptyList();
    }

    @Override
    public List<Player> getPossiblePlayers() {
        if(selectedPowerUp != null && targetPlayers.isEmpty())
            return this.playersFilter.apply(ownerPlayer, targetPlayers, targetSquares);
        return Collections.emptyList();
    }

    /**
     * This method assigns the shoot function of the input power up card to this class's shoot function, then it makes
     * the input power up card the selected one.
     * @param powerUpCard The powerUpCard to use.
     */
    @Override
    public void use(PowerUpCard powerUpCard) {
        if(getPossiblePowerUps().contains(powerUpCard)) {
            this.shootFunc = powerUpCard.shootFunc;
            this.selectedPowerUp = powerUpCard;
        }
    }

    @Override
    protected void preparePowerUp() {
        List<PowerUpCard> temp = new ArrayList<>(ownerPlayer.getPowerupSet().getCounterAttackPUs());
        temp.remove(selectedPowerUp);
        if(!temp.isEmpty()) {
            CounterPowerUpAction tmp = new CounterPowerUpAction();
            tmp.initialize(ownerPlayer);
            tmp.setTargets(damagedPlayers);
            ((Event<Action, Action>)createdActionEvent).invoke(this, tmp);
            this.next = tmp;
        }
    }

    public void setTargets(List<Player> targets)
    {
        damagedPlayers = new ArrayList<>(targets);
        this.playersFilter = (shooter, players, squares) -> targets;
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
