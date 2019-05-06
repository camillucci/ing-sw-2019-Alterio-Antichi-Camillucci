package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.SquareBorder.ROOM;
import static org.junit.jupiter.api.Assertions.*;

class AmmoSquareTest {
    private GameBoard gameBoard = new GameBoard(3, 11);
    private Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void grab() {
        assertEquals(1, player.getBlueAmmo());
        assertEquals(1, player.getYellowAmmo());
        assertEquals(1, player.getRedAmmo());
        assertEquals(0, player.getPowerUps().size());
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        int blue =  player.getBlueAmmo();
        int red =  player.getRedAmmo();
        int yellow = player.getYellowAmmo();
        int pu = player.getPowerUps().size();
        assertEquals(6, blue + red + yellow + pu);
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        assertEquals(blue, player.getBlueAmmo());
        assertEquals(red, player.getRedAmmo());
        assertEquals(yellow, player.getYellowAmmo());
        assertEquals(pu, player.getPowerUps().size());
        gameBoard.getSquares().get(0).refill();
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        assertTrue(6 <= player.getTotalAmmo() + player.getPowerUps().size());
        assertTrue(9 >= player.getTotalAmmo() + player.getPowerUps().size());
    }
}
