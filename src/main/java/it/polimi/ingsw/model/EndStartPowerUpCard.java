package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

public class EndStartPowerUpCard extends PowerUpCard
{
    public EndStartPowerUpCard(String name, AmmoColor color, Supplier<PowerUpAction> effect)
    {
        super(name, color, effect);
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
