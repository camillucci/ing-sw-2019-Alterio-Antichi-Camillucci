package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpDeckTest {

    PowerUpDeck powerUpDeck = new PowerUpDeck();
    PowerUpCard powerUpCard;

    @Test
    void drawAndAddDiscarded() {
        IntStream.range(0, 23).forEach(i -> {
            powerUpCard = powerUpDeck.draw();
            assertTrue(powerUpCard instanceof PowerUpCard);
        });
        powerUpCard = powerUpDeck.draw();
        powerUpDeck.addDiscarded(powerUpCard);
        assertTrue(powerUpDeck.draw() == powerUpCard);
    }
}