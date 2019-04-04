package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player("", PlayerColor.YELLOW, null);

    @Test
    void addRed() {
        player.addRed(1);
        assertEquals(2, player.getRedAmmo());
        player.addRed(1);
        assertEquals(3, player.getRedAmmo());
        player.addRed(1);
        assertEquals(3, player.getRedAmmo());
    }

    @Test
    void addBlue() {
        player.addBlue(1);
        assertEquals(2, player.getBlueAmmo());
        player.addBlue(1);
        assertEquals(3, player.getBlueAmmo());
        player.addBlue(1);
        assertEquals(3, player.getBlueAmmo());
    }

    @Test
    void addYellow() {
        player.addYellow(1);
        assertEquals(2, player.getYellowAmmo());
        player.addYellow(1);
        assertEquals(3, player.getYellowAmmo());
        player.addYellow(1);
        assertEquals(3, player.getYellowAmmo());
    }

}
