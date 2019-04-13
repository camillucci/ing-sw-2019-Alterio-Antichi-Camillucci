package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameBoard = new GameBoard(3, 10);
    private Player player = new Player("A", PlayerColor.YELLOW, gameBoard);
    private static final int N = 10;
    private static final int MAX_AMMO = 3;
    private static final int MAX_POWER_UPS = 3;
    private static final int MAX_POWER_UPS_RESPAWN = 4;
    private static final int MAX_DAMAGES = 12;

    @Test
    void addRed() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getRedAmmo());
            player.addRed(1);
        }
    }

    @Test
    void addBlue() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getBlueAmmo());
            player.addBlue(1);
        }
    }

    @Test
    void addYellow() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i + 1, MAX_AMMO), player.getYellowAmmo());
            player.addYellow(1);
        }
    }

    @Test
    void addPowerUpCardAndRemove() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i, MAX_POWER_UPS), player.getPowerUps().size());
            player.addPowerUpCard();
        }
        for(int i = MAX_POWER_UPS - 1; i >= 0; i--)
            player.removePowerUpCard(i);
    }

    @Test
    void addPowerUpCardRespawnAndRemove() {
        for(int i = 0; i < N; i++) {
            assertEquals(Math.min(i, MAX_POWER_UPS_RESPAWN), player.getPowerUps().size());
            player.addPowerUpCardRespawn();
        }
        for(int i = MAX_POWER_UPS_RESPAWN - 1; i >= 0; i--)
            player.removePowerUpCard(i);
    }

    @Test
    void addDamages() {
        Player player2 = new Player("B", PlayerColor.VIOLET, gameBoard);
        Player player3 = new Player("C", PlayerColor.GREEN, gameBoard);

        for(int i = 0; i < N * 2; i++) {
            assertEquals(Math.min(i * 3, MAX_DAMAGES), player.getDamage().size());
            player.addDamage(player2, 1);
            assertEquals(Math.min(i * 3 + 1, MAX_DAMAGES), player.getDamage().size());
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
