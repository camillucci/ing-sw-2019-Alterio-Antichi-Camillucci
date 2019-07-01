package it.polimi.ingsw.model.action;

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
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(selectedPowerUp);
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            }
        });
        this.visualizable = new Visualizable("use a Targeting Scope", "powerup");
    }

    @Override
    protected void preparePowerUp() {
        InTurnPowerUpAction tmp = new InTurnPowerUpAction();
        tmp.setTargets(damagedPlayers);
        this.next = tmp;
    }

    /**
     * Setse the targets this action is going to effect based on the input parameter.
     * @param targets List of players the card is going to be targeting.
     */
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
        return selectedPowerUp == null ? new ArrayList<>(ownerPlayer.getPowerupSet().getInTurnPUs()) : Collections.emptyList();
    }

    @Override
    public void use(PowerUpCard powerUpCard)
    {
        this.shootFunc = powerUpCard.shootFunc;
        this.selectedPowerUp = powerUpCard;
    }

    @Override
    public void addTarget(Player target)
    {
        targetPlayers.add(target);
        if(discardedAmmo != null)
            this.canBeDone = true;
    }

    /**
     * This method executes the discarding of a power up card by adding ammo the player's pool. It can only be completed
     * if the targets list is empty.
     * @param ammo Ammo card discarded by the player if they actually have it among they cards they control
     */
    @Override
    public void discard(Ammo ammo) {
        for(Ammo discardableAmmos : ownerPlayer.getDiscardableAmmo())
            if(discardableAmmos.isEqual(ammo)) {
                this.discardedAmmo = ammo;
                if(!targetPlayers.isEmpty())
                    this.canBeDone = true;
                return;
            }
    }

    @Override
    public List<Ammo> getDiscardableAmmos() {
        if(discardedAmmo != null)
            return Collections.emptyList();
        return ownerPlayer.getDiscardableAmmo();
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
