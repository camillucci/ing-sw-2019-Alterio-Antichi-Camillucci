package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class TargetsFiltersTest {

    private GameBoard gameBoard = new GameBoard(6, 12);
    private Player player = new Player("A", GREY, gameBoard);
    private Player p2;
    private Player p3;

    @BeforeEach
    void setUp() {
        p2 = new Player("B", YELLOW, gameBoard);
        p3 = new Player("C", VIOLET, gameBoard);
    }

    @Test
    void visiblePlayers() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.visiblePlayers(player, Collections.emptyList(), 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.visiblePlayers(player, Collections.emptyList(), 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.visiblePlayers(player, Collections.emptyList(), 1).size());
        p2.getCurrentSquare().removePlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(2, TargetsFilters.visiblePlayers(player, Collections.emptyList(), 1).size());
    }

    @Test
    void visibleSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.visibleSquares(player, Collections.emptyList(), 1).size());
        p2.setCurrentSquare(gameBoard.getSquares().get(1));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.visibleSquares(player, Collections.emptyList(), 1).size());
        p3.setCurrentSquare(gameBoard.getSquares().get(3));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, TargetsFilters.visibleSquares(player, Collections.emptyList(), 1).size());
        p2.getCurrentSquare().removePlayer(p2);
        p2.setCurrentSquare(gameBoard.getSquares().get(8));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.visibleSquares(player, Collections.emptyList(), 1).size());
    }

    @Test
    void awayPlayers() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.awayPlayers(player, Collections.emptyList(), 1, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.awayPlayers(player, Collections.emptyList(), 1, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.awayPlayers(player, Collections.emptyList(), 1, 1).size());
    }

    @Test
    void nearPlayers() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.nearPlayers(player, Collections.emptyList(), 1, 0).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.nearPlayers(player, Collections.emptyList(), 1, 0).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.nearPlayers(player, Collections.emptyList(), 1, 0).size());
    }

    @Test
    void betweenPlayers() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.betweenPlayers(player, Collections.emptyList(), 1, 1, 2).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.betweenPlayers(player, Collections.emptyList(), 1, 1, 2).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.betweenPlayers(player, Collections.emptyList(), 1, 1, 2).size());
    }

    /*
    @Test
    void awaySquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.awaySquares(player, Collections.emptyList(), 1, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.awaySquares(player, Collections.emptyList(), 1, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.awaySquares(player, Collections.emptyList(), 1, 1).size());
    }
    */

    @Test
    void nearSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.nearSquares(player, Collections.emptyList(), 1, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.nearSquares(player, Collections.emptyList(), 1, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.nearSquares(player, Collections.emptyList(), 1, 1).size());
    }

    @Test
    void betweenSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.betweenSquares(player, Collections.emptyList(), 1, 1, 2).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.betweenSquares(player, Collections.emptyList(), 1, 1, 2).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 2).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.betweenSquares(player, Collections.emptyList(), 1, 1, 2).size());
    }

    @Test
    void nonVisiblePlayers() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        gameBoard.setPlayers(Collections.singletonList(player));
        assertEquals(0, TargetsFilters.nonVisiblePlayers(player, Collections.emptyList(), 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        gameBoard.setPlayers(Arrays.asList(player, p2));
        assertEquals(1, TargetsFilters.nonVisiblePlayers(player, Collections.emptyList(), 1).size());
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p3.getCurrentSquare().addPlayer(p3);
        gameBoard.setPlayers(Arrays.asList(player, p2, p3));
        assertEquals(2, TargetsFilters.nonVisiblePlayers(player, Collections.emptyList(), 1).size());
    }

    @Test
    void otherVisibleRoom() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.otherVisibleRoom(player, Collections.emptyList(), 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.otherVisibleRoom(player, Collections.emptyList(), 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.otherVisibleRoom(player, Collections.emptyList(), 1).size());
    }
}
