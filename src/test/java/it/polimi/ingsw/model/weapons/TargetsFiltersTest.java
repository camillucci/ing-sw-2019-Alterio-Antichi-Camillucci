package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class TargetsFiltersTest {

    GameBoard gameBoard = new GameBoard(6, 12);
    Player player = new Player("A", GREY, gameBoard);

    @Test
    void visiblePlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.visiblePlayers(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.visiblePlayers(player).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.visiblePlayers(player).size());
        p2.getCurrentSquare().removePlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(2, TargetsFilters.visiblePlayers(player).size());
    }

    @Test
    void visibleSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        assertEquals(7, TargetsFilters.visibleSquares(player).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        assertEquals(4, TargetsFilters.visibleSquares(player).size());
        player.setCurrentSquare(gameBoard.getInRangeSquares(player).get(1));
        assertEquals(4, TargetsFilters.visibleSquares(player).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        assertEquals(4, TargetsFilters.visibleSquares(player).size());
        player.setCurrentSquare(gameBoard.getInRangeSquares(player).get(2));
        assertEquals(6, TargetsFilters.visibleSquares(player).size());
    }

    @Test
    void awayPlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.awayPlayers(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.awayPlayers(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.awayPlayers(player, 1).size());
    }

    @Test
    void nearPlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.nearPlayers(player, 0).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.nearPlayers(player, 0).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.nearPlayers(player, 0).size());
    }

    @Test
    void awaySquares() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.awaySquares(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, TargetsFilters.awaySquares(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.awaySquares(player, 1).size());
    }

    @Test
    void nearSquares() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, TargetsFilters.nearSquares(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, TargetsFilters.nearSquares(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, TargetsFilters.nearSquares(player, 1).size());
    }
}