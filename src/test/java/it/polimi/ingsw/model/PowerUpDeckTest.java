package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.PowerUpCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpDeckTest {

    private PowerUpDeck powerUpDeck = new PowerUpDeck();
    private static final int numberOfCards = 24;

    @Test
    void drawAndAddDiscarded() {
        PowerUpCard powerUpCard;
        for (int i = 0; i < numberOfCards - 1; i++) {
            powerUpCard = powerUpDeck.draw();
            assertNotNull(powerUpCard);
        }
        powerUpCard = powerUpDeck.draw();
        powerUpDeck.addDiscarded(powerUpCard);
        assertSame(powerUpDeck.draw(), powerUpCard);
    }
}
