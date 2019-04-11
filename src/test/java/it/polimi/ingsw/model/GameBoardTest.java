package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private static final int gameLength = 6;
    private static final int maxKillShotTrack = 8;
    GameBoard gameBoard = new GameBoard(gameLength, 10);

    @Test
    void addKillShotTrack() {
        assertEquals(2, gameBoard.getKillShotTrack().size());
        assertEquals(0, gameBoard.getKillShotTrack().get(0).size());
        assertEquals(0, gameBoard.getKillShotTrack().get(1).size());
        for (int i = 0; i < gameLength; i++) {
            gameBoard.addKillShotTrack(new ArrayList<>(Arrays.asList(BLUE, BLUE)));
            assertEquals(i + 3, gameBoard.getKillShotTrack().size());
            assertEquals(2, gameBoard.getKillShotTrack().get(i + 2).size());
        }
        assertEquals(2, gameBoard.getKillShotTrack().get(maxKillShotTrack - 1).size());
        for (int i = 0; i < gameLength - 1; i++) {
            gameBoard.addKillShotTrack(new ArrayList<>(Arrays.asList(BLUE, BLUE)));
            assertEquals(maxKillShotTrack, gameBoard.getKillShotTrack().size());
            assertEquals(4 + i * 2, gameBoard.getKillShotTrack().get(maxKillShotTrack - 1).size());
        }
    }

    @Test
    void getSquares() {
        assertTrue(true);
        //TODO
    }

    @Test
    void getInRangeSquares() {
        assertTrue(true);
        //TODO
    }

    @Test
    void getInRangePlayers() {
        assertTrue(true);
        //TODO
    }
}
