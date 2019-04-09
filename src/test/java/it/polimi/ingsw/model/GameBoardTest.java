package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    GameBoard gameBoard = new GameBoard(6, 10);

    @Test
    void addKillShotTrack() {
        assertEquals(2, gameBoard.getKillShotTrack().size());
        assertEquals(0, gameBoard.getKillShotTrack().get(0).size());
        assertEquals(0, gameBoard.getKillShotTrack().get(1).size());
        for (int i = 0; i < 6; i++) {
            gameBoard.addKillShotTrack(new ArrayList<>(Arrays.asList(BLUE, BLUE)));
            assertEquals(i + 3, gameBoard.getKillShotTrack().size());
            assertEquals(2, gameBoard.getKillShotTrack().get(i + 2).size());
        }
        assertEquals(2, gameBoard.getKillShotTrack().get(7).size());
        for (int i = 0; i < 5; i++) {
            gameBoard.addKillShotTrack(new ArrayList<>(Arrays.asList(BLUE, BLUE)));
            assertEquals(8, gameBoard.getKillShotTrack().size());
            assertEquals(4 + i * 2, gameBoard.getKillShotTrack().get(7).size());
        }
    }
}
