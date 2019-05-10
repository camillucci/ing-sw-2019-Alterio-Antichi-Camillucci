package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SupportPowerUpAction;
import it.polimi.ingsw.model.weapons.ShootFunc;

import java.util.ArrayList;

public class CounterAttackPowerUpCard extends PowerUpCard {

    public CounterAttackPowerUpCard(String name, AmmoColor color, ShootFunc shootFunc)
    {
        super(name, color, () -> new SupportPowerUpAction(p -> new ArrayList<>(p.getPowerupSet().getCounterAttackPUs()), shootFunc));
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
