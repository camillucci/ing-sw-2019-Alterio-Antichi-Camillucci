package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.SupportPowerUpAction;
import it.polimi.ingsw.model.weapons.ShootFunc;

import java.util.ArrayList;

public class CounterAttackPowerUpCard extends PowerUpCard {

    public CounterAttackPowerUpCard(String name, AmmoColor color, Ammo cost, ShootFunc shootFunc)
    {
        super(name, color, cost, () -> new SupportPowerUpAction(p -> new ArrayList<>(p.getPowerupSet().getCounterAttackPUs()), shootFunc));
    }

    @Override
    public void addTo(PowerupSet powerupSet) {
        powerupSet.add(this);
    }

    @Override
    public void removeFrom(PowerupSet powerupSet) {
        powerupSet.remove(this);
    }
}
