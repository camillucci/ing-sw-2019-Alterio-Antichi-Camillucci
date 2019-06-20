package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.ShootFunc;
import it.polimi.ingsw.model.cards.TargetsFilters;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class InTurnPowerUpAction extends PowerUpAction
{
    private Function<Player, List<PowerUpCard>> powerUpFilter;

    public InTurnPowerUpAction(Function<Player, List<PowerUpCard>> powerUpFilter, ShootFunc shootFunc)
    {
        super(TargetsFilters.noPlayersFilter, TargetsFilters.noSquaresFilter, shootFunc);
        this.powerUpFilter = powerUpFilter;
        this.completedActionEvent.addEventHandler((a,b)->{
            if(this.selectedPowerUp != null) {
                ownerPlayer.gameBoard.powerupDeck.addDiscarded(selectedPowerUp);
                ownerPlayer.getPowerupSet().remove(selectedPowerUp);
            }
        });
        this.text = "use a Targeting Scope";
    }

    @Override
    protected void preparePowerUp() {
        InTurnPowerUpAction tmp = (InTurnPowerUpAction)selectedPowerUp.getEffect();
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

    @Override
    public void add(Player target)
    {
        if(this.getPossiblePlayers().contains(target)) {
            targetPlayers.add(target);
            if(discardedAmmo != null)
                this.canBeDone = true;
        }
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
