package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpSet;

/**
 * This class represents a specific type of power up card.
 */
public class InTurnPowerUpCard extends PowerUpCard {

    public InTurnPowerUpCard(String name, AmmoColor color, ShootFunc shootFunc)
    {
        super(name, color, TargetsFilters.noPlayersFilter, TargetsFilters.noSquaresFilter, shootFunc);
    }

    /**
     * This method calls an add method from the power up set of cards the card needs to added to.
     * @param powerupSet List of power up cards
     */
    @Override
    public void addTo(PowerUpSet powerupSet) {
        powerupSet.add(this);
    }

    /**
     * This method calls an add method from the power up set of cards the card needs to removed from.
     * @param powerupSet List of power up cards
     */
    @Override
    public void removeFrom(PowerUpSet powerupSet) {
        powerupSet.remove(this);
    }
}
