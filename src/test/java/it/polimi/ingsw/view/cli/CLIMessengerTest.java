package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class CLIMessengerTest {

    @Test
    void intro() {
        CLIMessenger.intro();
        assertTrue(true);
    }

    @Test
    void smallMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 10);
        match.gameBoard.getPlayers().get(0).setCurrentSquare(match.gameBoard.getSquares().get(0));
        match.gameBoard.getPlayers().get(0).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(0));
        match.gameBoard.getPlayers().get(1).setCurrentSquare(match.gameBoard.getSquares().get(1));
        match.gameBoard.getPlayers().get(1).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(1));
        match.gameBoard.getPlayers().get(2).setCurrentSquare(match.gameBoard.getSquares().get(2));
        match.gameBoard.getPlayers().get(2).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(2));
        CLIMessenger.updateView(match.createSnapshot(0));
        assertTrue(true);
    }

    @Test
    void middleMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 11);
        match.gameBoard.getPlayers().get(0).setCurrentSquare(match.gameBoard.getSquares().get(0));
        match.gameBoard.getPlayers().get(0).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(0));
        match.gameBoard.getPlayers().get(1).setCurrentSquare(match.gameBoard.getSquares().get(1));
        match.gameBoard.getPlayers().get(1).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(1));
        match.gameBoard.getPlayers().get(2).setCurrentSquare(match.gameBoard.getSquares().get(2));
        match.gameBoard.getPlayers().get(2).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(2));
        CLIMessenger.updateView(match.createSnapshot(0));
        assertTrue(true);
    }

    @Test
    void largeMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 12);
        match.gameBoard.getPlayers().get(0).setCurrentSquare(match.gameBoard.getSquares().get(0));
        match.gameBoard.getPlayers().get(0).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(0));
        match.gameBoard.getPlayers().get(1).setCurrentSquare(match.gameBoard.getSquares().get(1));
        match.gameBoard.getPlayers().get(1).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(1));
        match.gameBoard.getPlayers().get(2).setCurrentSquare(match.gameBoard.getSquares().get(2));
        match.gameBoard.getPlayers().get(2).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(2));
        CLIMessenger.updateView(match.createSnapshot(0));
        assertTrue(true);
    }
}
