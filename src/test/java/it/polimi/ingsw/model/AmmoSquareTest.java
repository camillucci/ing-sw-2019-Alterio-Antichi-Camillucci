package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoSquareTest {
    private GameBoard gameBoard = new GameBoard(3, 11);
    private Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void grab() {
        assertEquals(1, player.getAmmo().blue);
        assertEquals(1, player.getAmmo().yellow);
        assertEquals(1, player.getAmmo().red);
        assertEquals(0, player.getPowerUps().size());
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        int blue =  player.getAmmo().blue;
        int red =  player.getAmmo().red;
        int yellow = player.getAmmo().yellow;
        int pu = player.getPowerUps().size();
        assertEquals(6, blue + red + yellow + pu);
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        assertEquals(blue, player.getAmmo().blue);
        assertEquals(red, player.getAmmo().red);
        assertEquals(yellow, player.getAmmo().yellow);
        assertEquals(pu, player.getPowerUps().size());
        gameBoard.getSquares().get(0).refill();
        assertEquals(1, gameBoard.getSquares().get(0).grab(player).size());
        assertTrue(6 <= player.getTotalAmmo() + player.getPowerUps().size());
        assertTrue(9 >= player.getTotalAmmo() + player.getPowerUps().size());
    }
}
