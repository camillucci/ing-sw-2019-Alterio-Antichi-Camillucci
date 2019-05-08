package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.ShootFunc;

import java.util.List;
import java.util.function.Function;

public class SupportPowerUpAction extends PowerUpAction
{
    private Function<Player, List<PowerUpCard>> powerUpFilter;
    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter, ShootFunc shootFunc)
    {
        super(null, null, shootFunc);
        this.powerUpFilter = powerUpFilter;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
        });
    }

    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter)
    {
        this.powerUpFilter = powerUpFilter;
    }

    @Override
    protected void preparePowerUp() {
        super.preparePowerUp();
        ((SupportPowerUpAction)next).setTargets(getPossiblePlayers());
    }

    public void setTargets(List<Player> targets)
    {
        this.playersFilter = (shooter, players, squares) -> targets;
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return powerUpFilter.apply(ownerPlayer);
    }
}
