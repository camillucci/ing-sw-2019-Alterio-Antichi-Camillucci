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
    void firstMap() {
        Match match = new Match(Arrays.asList("Jerry", "Jimmy", "John", "James", "Johnny"), Arrays.asList(BLUE, GREEN, GREY, VIOLET, YELLOW), 5, 0);
        match.gameBoard.getPlayers().get(0).setCurrentSquare(match.gameBoard.getSquares().get(0));
        match.gameBoard.getPlayers().get(0).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(0));
        match.gameBoard.getPlayers().get(0).addDamage(match.gameBoard.getPlayers().get(1), 3);
        match.gameBoard.getPlayers().get(0).addMark(match.gameBoard.getPlayers().get(2), 2);
        match.gameBoard.getPlayers().get(1).setCurrentSquare(match.gameBoard.getSquares().get(1));
        match.gameBoard.getPlayers().get(1).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(1));
        match.gameBoard.getPlayers().get(1).addDamage(match.gameBoard.getPlayers().get(2), 3);
        match.gameBoard.getPlayers().get(1).addMark(match.gameBoard.getPlayers().get(3), 2);
        match.gameBoard.getPlayers().get(2).setCurrentSquare(match.gameBoard.getSquares().get(2));
        match.gameBoard.getPlayers().get(2).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(2));
        match.gameBoard.getPlayers().get(2).addDamage(match.gameBoard.getPlayers().get(3), 3);
        match.gameBoard.getPlayers().get(2).addMark(match.gameBoard.getPlayers().get(4), 2);
        match.gameBoard.getPlayers().get(3).setCurrentSquare(match.gameBoard.getSquares().get(3));
        match.gameBoard.getPlayers().get(3).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(3));
        match.gameBoard.getPlayers().get(3).addDamage(match.gameBoard.getPlayers().get(4), 3);
        match.gameBoard.getPlayers().get(3).addMark(match.gameBoard.getPlayers().get(0), 2);
        match.gameBoard.getPlayers().get(4).setCurrentSquare(match.gameBoard.getSquares().get(4));
        match.gameBoard.getPlayers().get(4).getCurrentSquare().addPlayer(match.gameBoard.getPlayers().get(4));
        match.gameBoard.getPlayers().get(4).addDamage(match.gameBoard.getPlayers().get(0), 3);
        match.gameBoard.getPlayers().get(4).addMark(match.gameBoard.getPlayers().get(1), 2);
        CLIMessenger.updateView(match.createSnapshot(0));
        assertTrue(true);
    }

    @Test
    void secondMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 1);
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
    void thirdMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 2);
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
    void fourthMap() {
        Match match = new Match(Arrays.asList("ThisIsAVLongName", "Jimmy", "John"), Arrays.asList(VIOLET, GREY, YELLOW), 5, 3);
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
