package it.polimi.ingsw.model.action;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is a specific case of a power up action class. It contains all the generic methods relative to an action
 * that can be executed by the current turn's player.
 */
public class InTurnPowerUpAction extends PowerUpAction
{
    public InTurnPowerUpAction()
    {
        super();
        this.visualizable = new Visualizable("use a Targeting Scope", "powerup");
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs()) : Collections.emptyList();
    }

    @Override
    public List<Player> getPossiblePlayers() {
        if(selectedPowerUp != null && targetPlayers.isEmpty())
            return this.playersFilter.apply(ownerPlayer, targetPlayers, targetSquares);
        return Collections.emptyList();
    }

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

    @Override
    public void addTarget(Player target)
    {
        if(getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            if (discardedAmmo != null || !discardedPowerUps.isEmpty())
                this.canBeDone = true;
        }
    }

    @Override
    public void addPowerUp(PowerUpCard powerUpCard) {
        if(getDiscardablePowerUps().contains(powerUpCard)) {
            discardedPowerUps.add(powerUpCard);
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
        damagedPlayers = targets;
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
