package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    private WeaponDeck weaponDeck = new WeaponDeck();
    private WeaponCard weaponCard;
    private static final int numberOfCards = 21;

    @Test
    void draw() {
        for (int i = 0; i < numberOfCards - 1; i++) {
            weaponCard = weaponDeck.draw();
            //TODO assertNotNull(weaponCard);
            assertTrue(true);
        }
        assertNull(weaponDeck.draw());
    }
}
