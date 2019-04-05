package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.SquareBorder.ROOM;
import static org.junit.jupiter.api.Assertions.*;

class AmmoSquareTest {
    GameBoard gameBoard = new GameBoard(3, 10);
    AmmoCard ammoCard = new AmmoCard(0, 1, 2, true);
    AmmoSquare ammoSquare = new AmmoSquare(ROOM, ROOM, ROOM, ROOM, ammoCard);
    Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void grab() {
        assertEquals(1, player.getBlueAmmo());
        assertEquals(1, player.getYellowAmmo());
        assertEquals(1, player.getRedAmmo());
        assertEquals(0, player.getPowerups().size());
        ammoSquare.grab(player);
        assertEquals(1, player.getBlueAmmo());
        assertEquals(2, player.getYellowAmmo());
        assertEquals(3, player.getRedAmmo());
        assertEquals(1, player.getPowerups().size());
        ammoSquare.grab(player);
        assertEquals(1, player.getBlueAmmo());
        assertEquals(2, player.getYellowAmmo());
        assertEquals(3, player.getRedAmmo());
        assertEquals(1, player.getPowerups().size());
    }

    @Test
    void isEmpty() {
        assertEquals(false, ammoSquare.isEmpty());
        ammoSquare.grab(player);
        assertEquals(true, ammoSquare.isEmpty());
        ammoSquare.setAmmoCard(ammoCard);
        assertEquals(false, ammoSquare.isEmpty());
    }
}