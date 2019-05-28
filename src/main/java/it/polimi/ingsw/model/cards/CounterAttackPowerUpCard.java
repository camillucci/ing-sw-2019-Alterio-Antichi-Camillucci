package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;
import it.polimi.ingsw.model.action.CounterPowerUpAction;

import java.util.ArrayList;

public class CounterAttackPowerUpCard extends PowerUpCard {

    public CounterAttackPowerUpCard(String name, AmmoColor color, ShootFunc shootFunc)
    {
        super(name, color, () -> new CounterPowerUpAction(p -> new ArrayList<>(p.getPowerupSet().getCounterAttackPUs()), shootFunc));
    }

    @Override
    public void addTo(PowerUpSet powerupSet) {
        powerupSet.add(this);
    }

    @Override
    public void removeFrom(PowerUpSet powerupSet) {
        powerupSet.remove(this);
    }
}
