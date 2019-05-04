package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.ShootFunc;

import java.util.List;
import java.util.function.Function;

public class SupportPowerUpAction extends PowerUpAction
{
    private Function<Player, List<PowerUpCard>> possiblePUFunc;
    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> possiblePUsFunc, ShootFunc shootFunc)
    {
        super(null, null, shootFunc);
        this.possiblePUFunc = possiblePUsFunc;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null)
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
        });
    }

    public SupportPowerUpAction(Function<Player, List<PowerUpCard>> possiblePUFunc)
    {
        this.possiblePUFunc = possiblePUFunc;
    }

    @Override
    protected void preparePowerUp() {
        super.preparePowerUp();
        ((SupportPowerUpAction)next).setTargets(getPossiblePlayers());
    }

    public void setTargets(List<Player> targets)
    {
        this.playersFilter = (shooter, players) -> targets;
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps() {
        return possiblePUFunc.apply(ownerPlayer);
    }
}
