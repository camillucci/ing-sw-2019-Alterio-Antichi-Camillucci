package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameBoard = new GameBoard(3, 10);
    private Player player = new Player("A", PlayerColor.YELLOW, gameBoard);
    private static final int n = 10;

    @Test
    void addRed() {
        for(int i = 0; i < n; i++) {
            assertEquals(Math.min(i + 1, 3), player.getRedAmmo());
            player.addRed(1);
        }
    }

    @Test
    void addBlue() {
        for(int i = 0; i < n; i++) {
            assertEquals(Math.min(i + 1, 3), player.getBlueAmmo());
            player.addBlue(1);
        }
    }

    @Test
    void addYellow() {
        for(int i = 0; i < n; i++) {
            assertEquals(Math.min(i + 1, 3), player.getYellowAmmo());
            player.addYellow(1);
        }
    }

    @Test
    void addPowerUpCard() {
        for(int i = 0; i < n; i++) {
            assertEquals(Math.min(i, 3), player.getPowerUps().size());
            player.addPowerUpCard();
        }
    }

    @Test
    void addDamages() {
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        Player player3 = new Player("C", PlayerColor.GREEN, gameBoard);

        for(int i = 0; i < n * 2; i++) {
            assertEquals(Math.min(i * 3, 12), player.getDamage().size());
            player.addDamage(player2, 1);
            assertEquals(Math.min(i * 3 + 1, 12), player.getDamage().size());
            player.addDamage(player3, 2);
        }
    }

    @Test
    void addMark() {
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        Player player3 = new Player("C", PlayerColor.GREEN, gameBoard);

        assertEquals(0, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(1, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(3, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(4, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(5, player3.getMark().size());
        player3.addMark(player, 1);
        assertEquals(6, player3.getMark().size());
        player3.addMark(player2, 2);
        assertEquals(6, player3.getMark().size());

        assertEquals(0, player3.getDamage().size());
        player3.addDamage(player, 1);
        assertEquals(4, player3.getDamage().size());

    }
}
