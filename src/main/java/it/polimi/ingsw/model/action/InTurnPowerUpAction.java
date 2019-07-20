package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.generics.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is a specific case of a power up action class. It contains all the generic methods relative to an action
 * that can be executed by the current turn's player and relative to the actions created by a power up card.
 */
public class InTurnPowerUpAction extends PowerUpAction
{
    /**
     * Constructor that uses default values for description and name parameters
     */
    public InTurnPowerUpAction()
    {
        super();
        this.visualizable = new Visualizable("use a Targeting Scope", "Powerup");
    }

    /**
     * Calculates the list of InTurnPowerUp action cards available to the player
     * @return the list of InTurnPowerUp action cards available to the player
     */
    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs()) : Collections.emptyList();
    }

    /**
     * Calculates the list of players that can be selected as targets while using an InTurnPowerUp action card
     * @return the list of players that can be selected as targets while using an InTurnPowerUp action card
     */
    @Override
    public List<Player> getPossiblePlayers() {
        if(selectedPowerUp != null && targetPlayers.isEmpty())
            return this.playersFilter.apply(ownerPlayer, targetPlayers, targetSquares);
        return Collections.emptyList();
    }

    /**
     * Calculates the list of power up cards that can be used by the player to pay for costs by discarding them.
     * @return The list of power up cards that can be discarded by the user. Empty list if there  aren't power up
     * cards available
     */
    @Override
    public List<PowerUpCard> getDiscardablePowerUps() {
        if(selectedPowerUp != null && discardedAmmo == null && discardedPowerUps.isEmpty()) {
            List<PowerUpCard> temp = ownerPlayer.getPowerUps();
            temp.remove(selectedPowerUp);
            return temp;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Ammo> getDiscardableAmmos() {
        if(selectedPowerUp != null && discardedAmmo == null && discardedPowerUps.isEmpty())
            return ownerPlayer.getDiscardableAmmo();
        return Collections.emptyList();
    }

    @Override
    public void use(PowerUpCard powerUpCard)
    {
        if(getPossiblePowerUps().contains(powerUpCard)) {
            this.shootFunc = powerUpCard.shootFunc;
            this.selectedPowerUp = powerUpCard;
        }
    }

    /**
     * Method adds the input parameter to the list of targets effected by the power up card used by the player.
     * The player is added only if the chosen target is legal.
     * @param target Player added as a target for the effect of the in-turn power up card used by the current player.
     */
    @Override
    public void addTarget(Player target)
    {
        if(getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            if(discardedAmmo != null || !discardedPowerUps.isEmpty())
                this.canBeDone = true;
        }
    }

    @Override
    public void addPowerUp(PowerUpCard powerUpCard) {
        if(getDiscardablePowerUps().contains(powerUpCard)) {
            discardedPowerUps.add(powerUpCard);
            discardedAmmo = powerUpCard.colorToAmmo();
            if(!targetPlayers.isEmpty())
                this.canBeDone = true;
        }
    }

    /**
     * This method executes the discarding of a power up card by adding ammo the player's pool. It can only be completed
     * if the targets list is empty.
     * @param ammo Ammo card discarded by the player if they actually have it among they cards they control
     */
    @Override
    public void discard(Ammo ammo) {
        if(ammo.isLessOrEqualThan(ownerPlayer.getAmmo())) {
            this.discardedAmmo = ammo;
            if(!targetPlayers.isEmpty())
                this.canBeDone = true;
        }
    }

    @Override
    protected void preparePowerUp() {
        List<PowerUpCard> temp = new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs());
        temp.remove(selectedPowerUp);
        if(!temp.isEmpty()) {
            InTurnPowerUpAction tmp = new InTurnPowerUpAction();
            tmp.initialize(ownerPlayer);
            tmp.setTargets(damagedPlayers);
            ((Event<Action, Action>)createdActionEvent).invoke(this, tmp);
            this.next = tmp;
        }
    }

    /**
     * Sets the targets this action is going to effect based on the input parameter.
     * @param targets List of players the card is going to be targeting.
     */
    public void setTargets(List<Player> targets)
    {
        damagedPlayers = new ArrayList<>(targets);
        this.playersFilter = (shooter, players, squares) -> targets;
    }

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    protected boolean testCompatibilityWith(InTurnPowerUpAction action) {
        return true;
    }

    @Override
    public boolean testCompatibilityWith(PowerUpAction action) {
        return false;
    }
}
