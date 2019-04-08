package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoDeckTest {

    AmmoDeck ammoDeck = new AmmoDeck();
    AmmoCard ammoCard;

    @Test
    void drawAndAddDiscarded() {
        for (int i = 0; i < 35; i++) {
            ammoCard = ammoDeck.draw();
            assertTrue(ammoCard instanceof AmmoCard);
        }
        ammoCard = ammoDeck.draw();
        ammoDeck.addDiscarded(ammoCard);
        assertTrue( ammoDeck.draw() == ammoCard);
    }
}