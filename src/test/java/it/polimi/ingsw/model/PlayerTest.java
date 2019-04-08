package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private GameBoard gameBoard = new GameBoard(3, 10);
    private Player player = new Player("", PlayerColor.YELLOW, gameBoard);

    @Test
    void addRed() {
        for(int i = 0; i < 10; i++) {
            assertEquals(Math.min(i + 1, 3), player.getRedAmmo());
            player.addRed(1);
        }
    }

    @Test
    void addBlue() {
        for(int i = 0; i < 10; i++) {
            assertEquals(Math.min(i + 1, 3), player.getBlueAmmo());
            player.addBlue(1);
        }
    }

    @Test
    void addYellow() {
        for(int i = 0; i < 10; i++) {
            assertEquals(Math.min(i + 1, 3), player.getYellowAmmo());
            player.addYellow(1);
        }
    }

    @Test
    void addPowerUpCard() {
        for(int i = 0; i < 10; i++) {
            assertEquals(Math.min(i, 3), player.getPowerUps().size());
            player.addPowerUpCard();
        }
    }

    @Test
    void addDamages() {
        for(int i = 0; i < 20; i++) {
            assertEquals(Math.min(i * 3, 12), player.getDamage().size());
            player.addDamages(VIOLET, 1);
            assertEquals(Math.min(i * 3 + 1, 12), player.getDamage().size());
            player.addDamages(YELLOW, 2);
        }
    }
}
