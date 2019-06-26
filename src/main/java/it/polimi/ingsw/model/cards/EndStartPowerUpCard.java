package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;
import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

public class EndStartPowerUpCard extends PowerUpCard
{
    public EndStartPowerUpCard(String name, AmmoColor color, PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        super(name, color, playersFilter, squaresFilter, shootFunc);
    }
    @Override
    public void addTo(PowerUpSet powerupSet)
    {
        powerupSet.add(this);
    }

    @Override
    public void removeFrom(PowerUpSet powerupSet) {
        powerupSet.remove(this);
    }
}
