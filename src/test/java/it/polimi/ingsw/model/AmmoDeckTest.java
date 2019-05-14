package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.AmmoCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoDeckTest {

    private AmmoDeck ammoDeck = new AmmoDeck();
    private static final int numberOfCards = 36;

    @Test
    void drawAndAddDiscarded() {
        AmmoCard ammoCard;
        for (int i = 0; i < numberOfCards - 1; i++) {
            ammoCard = ammoDeck.draw();
            assertNotNull(ammoCard);
        }
        ammoCard = ammoDeck.draw();
        ammoDeck.addDiscarded(ammoCard);
        assertSame(ammoDeck.draw(), ammoCard);
    }
}
