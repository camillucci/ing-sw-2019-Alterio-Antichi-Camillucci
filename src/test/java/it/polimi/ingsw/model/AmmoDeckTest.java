package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class AmmoDeckTest {

    AmmoDeck ammoDeck = new AmmoDeck();
    AmmoCard ammoCard;

    @Test
    void drawAndAddDiscarded() {
        IntStream.range(0, 35).forEach(i -> {
            ammoCard = ammoDeck.draw();
            assertTrue(ammoCard instanceof AmmoCard);
        });
        ammoCard = ammoDeck.draw();
        ammoDeck.addDiscarded(ammoCard);
        assertTrue( ammoDeck.draw() == ammoCard);
    }
}