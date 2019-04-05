package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    GameBoard gameBoard = new GameBoard(3, 10);
    Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void addRed() {
        assertEquals(1, player.getRedAmmo());
        player.addRed(1);
        assertEquals(2, player.getRedAmmo());
        player.addRed(1);
        assertEquals(3, player.getRedAmmo());
        player.addRed(1);
        assertEquals(3, player.getRedAmmo());
    }

    @Test
    void addBlue() {
        assertEquals(1, player.getRedAmmo());
        player.addBlue(1);
        assertEquals(2, player.getBlueAmmo());
        player.addBlue(1);
        assertEquals(3, player.getBlueAmmo());
        player.addBlue(1);
        assertEquals(3, player.getBlueAmmo());
    }

    @Test
    void addYellow() {
        assertEquals(1, player.getRedAmmo());
        player.addYellow(1);
        assertEquals(2, player.getYellowAmmo());
        player.addYellow(1);
        assertEquals(3, player.getYellowAmmo());
        player.addYellow(1);
        assertEquals(3, player.getYellowAmmo());
    }

    @Test
    void addPowerUpCard() {
        assertEquals(0, player.getPowerups().size());
        player.addPowerUpCard();
        assertEquals(1, player.getPowerups().size());
        player.addPowerUpCard();
        assertEquals(2, player.getPowerups().size());
        player.addPowerUpCard();
        assertEquals(3, player.getPowerups().size());
        player.addPowerUpCard();
        assertEquals(3, player.getPowerups().size());
    }
}
