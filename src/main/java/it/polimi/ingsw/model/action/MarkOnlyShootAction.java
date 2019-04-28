package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.PowerUpCard;
import it.polimi.ingsw.model.weapons.PlayersFilter;
import it.polimi.ingsw.model.weapons.ShootFunc;
import it.polimi.ingsw.model.weapons.SquaresFilter;

import java.util.Collections;
import java.util.List;

public class MarkOnlyShootAction extends ShootAction
{
    public MarkOnlyShootAction(ShootFunc shootFunc, SquaresFilter squaresFilter)
    {
        this(null, squaresFilter, shootFunc);
    }

    public MarkOnlyShootAction(PlayersFilter playersFilter, ShootFunc shootFunc)
    {
        this(playersFilter, null, shootFunc);
    }

    public MarkOnlyShootAction(PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(playersFilter, squaresFilter, shootFunc);
    }


    @Override
    public List<PowerUpCard> getPossiblePowerUps(){
        return Collections.emptyList();
    }

    @Override
    public void addPowerUp(PowerUpCard powerUp)
    {
        // Nothing
    }
}
