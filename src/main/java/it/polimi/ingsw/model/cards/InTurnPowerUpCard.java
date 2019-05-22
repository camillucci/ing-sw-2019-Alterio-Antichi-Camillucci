package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;
import it.polimi.ingsw.model.action.SupportPowerUpAction;

import java.util.ArrayList;

public class InTurnPowerUpCard extends PowerUpCard {

    public InTurnPowerUpCard(String name, AmmoColor color, ShootFunc shootFunc)
    {
        super(name, color, () -> new SupportPowerUpAction(p -> new ArrayList<>(p.getPowerupSet().getInTurnPUs()), shootFunc));
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
