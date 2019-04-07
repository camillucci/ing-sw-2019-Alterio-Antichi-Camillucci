package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameBoard = new GameBoard(3, 10);
    private Player player = new Player("", PlayerColor.YELLOW, gameBoard);

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
        assertEquals(0, player.getPowerUps().size());
        player.addPowerUpCard();
        assertEquals(1, player.getPowerUps().size());
        player.addPowerUpCard();
        assertEquals(2, player.getPowerUps().size());
        player.addPowerUpCard();
        assertEquals(3, player.getPowerUps().size());
        player.addPowerUpCard();
        assertEquals(3, player.getPowerUps().size());
    }
}
