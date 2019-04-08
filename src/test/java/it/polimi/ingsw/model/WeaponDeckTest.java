package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponDeckTest {

    WeaponDeck weaponDeck = new WeaponDeck();
    WeaponCard weaponCard;

    @Test
    void draw() {
        for (int i = 0; i < 2; i++) { //TODO Change number to 21 when cards are added
            weaponCard = weaponDeck.draw();
            assertTrue(weaponCard instanceof WeaponCard);
        }
        assertTrue(weaponDeck.draw() == null);
    }
}