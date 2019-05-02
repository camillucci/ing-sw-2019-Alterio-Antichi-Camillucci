package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.PlayersFilter;
import it.polimi.ingsw.model.weapons.ShootFunc;
import it.polimi.ingsw.model.weapons.SquaresFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PowerUpAction extends ShootAction
{
    public final Type type;

    public PowerUpAction(ShootFunc shootFunc, Type type)
    {
        super(null, null, shootFunc);
        this.type = type;
    }

    // End-Turn powerUp Constructor
    public PowerUpAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(playersFilter, squaresFilter, shootFunc);
        type = Type.END_START_MOVE;
    }

    public PowerUpAction(Type type)
    {
        this.type = type;
    }

    @Override
    protected void preparePowerUp()
    {
        PowerUpAction tmp = selectedPowerUp.getEffect();
        if(selectedPowerUp.getEffect().type == Type.IN_TURN)
            tmp.setTargets(damagedPlayers);
        this.next = tmp;
    }

    @Override
    public List<PowerUpCard> getPossiblePowerUps()
    {
        if(selectedPowerUp != null && ownerPlayer.getTotalAmmo() >= 1)
            return ownerPlayer.getPowerUps().stream().filter(pu -> pu.getEffect().type == this.type).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public void setTargets(List<Player> targets)
    {
        this.playersFilter = (shooter, players) -> new ArrayList<>(targets);
    }

    public enum Type
    {
        COUNTER_ATTACK,
        END_START_MOVE,
        IN_TURN
    }
}
