package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;

import java.util.function.Supplier;

public class EndStartPowerUpCard extends PowerUpCard
{
    public EndStartPowerUpCard(String name, AmmoColor color, Ammo cost, Supplier<PowerUpAction> effect)
    {
        super(name, color, cost, effect);
    }
    @Override
    public void addTo(PowerupSet powerupSet)
    {
        powerupSet.add(this);
    }

    @Override
    public void removeFrom(PowerupSet powerupSet) {
        powerupSet.remove(this);
    }
}
