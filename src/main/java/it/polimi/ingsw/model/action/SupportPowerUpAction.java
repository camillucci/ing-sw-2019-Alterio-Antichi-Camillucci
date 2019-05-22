package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.TargetsFilters;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SupportPowerUpAction extends PowerUpAction
{
    private Function<Player, List<PowerUpCard>> powerUpFilter;

    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter, ShootFunc shootFunc)
    {
        super(TargetsFilters.noPlayersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.powerUpFilter = powerUpFilter;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(selectedPowerUp);
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            }
        });
    }

    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter)
    {
        this.playersFilter = TargetsFilters.noPlayersFilter;
        this.squaresFilter = TargetsFilters.noSquaresFilter;
        this.powerUpFilter = powerUpFilter;
    }

    @Override
    protected void preparePowerUp() {
        SupportPowerUpAction tmp = (SupportPowerUpAction)selectedPowerUp.getEffect();
        tmp.setTargets(damagedPlayers);
        tmp.damagedPlayers = damagedPlayers;
        this.next = tmp;
    }

    public void setTargets(List<Player> targets)
    {
        this.playersFilter = (shooter, players, squares) -> {
            if(targetPlayers.isEmpty())
                return targets;
            return Collections.emptyList();
        };
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return powerUpFilter.apply(ownerPlayer);
    }
}
