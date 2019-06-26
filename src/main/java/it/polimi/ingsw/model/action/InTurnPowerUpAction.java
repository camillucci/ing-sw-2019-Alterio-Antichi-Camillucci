package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Visualizable;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.TargetsFilters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
