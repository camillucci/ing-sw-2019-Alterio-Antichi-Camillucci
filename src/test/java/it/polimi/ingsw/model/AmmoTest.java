package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoTest {

    private Ammo ammo1 = new Ammo(0, 1, 2);
    private Ammo ammo2 = new Ammo(2, 2, 3);
    private Ammo ammo3 = new Ammo(0, 1, 3);
    private Player p = new Player("A", PlayerColor.GREEN, null);

    @Test
    void sub() {
        assertTrue(ammo1.isEqual(new Ammo(3, 3, 3).sub(new Ammo(3, 2, 1))));
    }

    @Test
    void add() {
        assertTrue(ammo2.isEqual(new Ammo(0, 1, 3).add(new Ammo(2, 1, 0))));
    }

    @Test
    void isLessThan() {
        assertTrue(ammo1.isLessThan(ammo2));
        assertTrue(ammo1.isLessThan(ammo3));
        assertFalse(ammo1.isLessThan(ammo1));
        assertFalse(ammo2.isLessThan(ammo3));
    }

    @Test
    void isLessOrEqualThan() {
        assertTrue(ammo1.isLessOrEqualThan(ammo2));
        assertTrue(ammo1.isLessOrEqualThan(ammo3));
        assertTrue(ammo1.isLessOrEqualThan(ammo1));
        assertFalse(ammo2.isLessOrEqualThan(ammo3));
    }

    @Test
    void isGreaterOrEqual() {
        assertFalse(ammo1.isGreaterOrEqual(ammo2));
        assertFalse(ammo1.isGreaterOrEqual(ammo3));
        assertTrue(ammo1.isGreaterOrEqual(ammo1));
        assertTrue(ammo2.isGreaterOrEqual(ammo3));
    }

    @Test
    void isGreaterThan() {
        assertFalse(ammo1.isGreaterThan(ammo2));
        assertFalse(ammo1.isGreaterThan(ammo3));
        assertFalse(ammo1.isGreaterThan(ammo1));
        assertTrue(ammo2.isGreaterThan(ammo3));
    }

    @Test
    void getAmmo() {
        assertTrue(new Ammo(1, 1, 1).isEqual(p.getAmmo()));
    }

    @Test
    void isEqual() {
        assertFalse(ammo1.isEqual(ammo2));
        assertFalse(ammo1.isEqual(ammo3));
        assertTrue(ammo1.isEqual(ammo1));
        assertFalse(ammo2.isEqual(ammo3));
    }
}
