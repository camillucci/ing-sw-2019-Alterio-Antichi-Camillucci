package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpDeckTest {

    PowerUpDeck powerUpDeck = new PowerUpDeck();
    PowerUpCard powerUpCard;
    private static final int numberOfCards = 24;

    @Test
    void drawAndAddDiscarded() {
        for (int i = 0; i < numberOfCards - 1; i++) {
            powerUpCard = powerUpDeck.draw();
            assertTrue(powerUpCard instanceof PowerUpCard);
        }
        powerUpCard = powerUpDeck.draw();
        powerUpDeck.addDiscarded(powerUpCard);
        assertTrue(powerUpDeck.draw() == powerUpCard);
    }
}
