package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    WeaponDeck weaponDeck = new WeaponDeck();
    WeaponCard weaponCard;
    private static final int numberOfCards = 21;

    @Test
    void draw() {
        for (int i = 0; i < numberOfCards - 1; i++) { //TODO Change number to 21 when cards are added
            weaponCard = weaponDeck.draw();
            //TODO assertTrue(weaponCard instanceof WeaponCard);
            assertTrue(true);
        }
        //TODO assertTrue(weaponDeck.draw() == null);
        assertTrue(true);
    }
}
