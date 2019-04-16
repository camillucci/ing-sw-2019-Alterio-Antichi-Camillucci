package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.SquareBorder.ROOM;
import static org.junit.jupiter.api.Assertions.*;

class AmmoSquareTest {
    private GameBoard gameBoard = new GameBoard(3, 11);
    private AmmoCard ammoCard = new AmmoCard(0, 1, 2, true);
    private AmmoSquare ammoSquare = new AmmoSquare(0 ,0, new SquareBorder[]{ROOM, ROOM, ROOM, ROOM}, ammoCard);
    private Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void grab() {
        assertEquals(1, player.getBlueAmmo());
        assertEquals(1, player.getYellowAmmo());
        assertEquals(1, player.getRedAmmo());
        assertEquals(0, player.getPowerUps().size());
        assertEquals(0, ammoSquare.grab(player).size());
        assertEquals(1, player.getBlueAmmo());
        assertEquals(2, player.getYellowAmmo());
        assertEquals(3, player.getRedAmmo());
        assertEquals(1, player.getPowerUps().size());
        assertEquals(0, ammoSquare.grab(player).size());
        assertEquals(1, player.getBlueAmmo());
        assertEquals(2, player.getYellowAmmo());
        assertEquals(3, player.getRedAmmo());
        assertEquals(1, player.getPowerUps().size());
    }

    @Test
    void isEmpty() {
        assertFalse(ammoSquare.isEmpty());
        assertEquals(0, ammoSquare.grab(player).size());
        assertTrue(ammoSquare.isEmpty());
        ammoSquare.setAmmoCard(ammoCard);
        assertFalse(ammoSquare.isEmpty());
    }
}
