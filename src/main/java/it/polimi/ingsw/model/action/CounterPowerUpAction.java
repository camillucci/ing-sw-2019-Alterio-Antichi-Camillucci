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

public class CounterPowerUpAction extends PowerUpAction
{
    public CounterPowerUpAction()
    {
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(selectedPowerUp);
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

    @Override
    public boolean isCompatible(Action action) {
        return action.testCompatibilityWith(this);
    }

    @Override
    public boolean testCompatibilityWith(PowerUpAction action) {
        return false;
    }

    @Override
    public boolean testCompatibilityWith(CounterPowerUpAction action) {
        return true;
    }
}
