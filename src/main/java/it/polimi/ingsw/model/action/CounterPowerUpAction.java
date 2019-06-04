package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.TargetsFilters;

import java.util.List;
import java.util.function.Function;

public class CounterPowerUpAction extends PowerUpAction
{
    private Function<Player, List<PowerUpCard>> powerUpFilter;

    public CounterPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter, ShootFunc shootFunc)
    {
        super(TargetsFilters.noPlayersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.powerUpFilter = powerUpFilter;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(selectedPowerUp);
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            }
        });
        this.text = "use a Tagback Grenade";
    }

    public CounterPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter)
    {
        this.playersFilter = TargetsFilters.noPlayersFilter;
        this.squaresFilter = TargetsFilters.noSquaresFilter;
        this.powerUpFilter = powerUpFilter;
        this.text = "use a Tagback Grenade";
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return powerUpFilter.apply(ownerPlayer);
    }
}
