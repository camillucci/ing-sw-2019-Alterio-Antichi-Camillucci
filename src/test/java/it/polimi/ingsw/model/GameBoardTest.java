package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    GameBoard gameBoard = new GameBoard(gameLength, 12);
    Player player = new Player("A", GREY, gameBoard);
    private static final int gameLength = 6;
    private static final int maxKillShotTrack = 8;

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
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        assertEquals(0, gameBoard.getSquares(player, -1).size());
        assertEquals(1, gameBoard.getSquares(player, 0).size());
        assertEquals(4, gameBoard.getSquares(player, 1).size());
        assertEquals(8, gameBoard.getSquares(player, 2).size());
        assertEquals(11, gameBoard.getSquares(player, 3).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        assertEquals(0, gameBoard.getSquares(player, -1).size());
        assertEquals(1, gameBoard.getSquares(player, 0).size());
        assertEquals(3, gameBoard.getSquares(player, 1).size());
        assertEquals(5, gameBoard.getSquares(player, 2).size());
        assertEquals(8, gameBoard.getSquares(player, 3).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        assertEquals(0, gameBoard.getSquares(player, -1).size());
        assertEquals(1, gameBoard.getSquares(player, 0).size());
        assertEquals(3, gameBoard.getSquares(player, 1).size());
        assertEquals(6, gameBoard.getSquares(player, 2).size());
        assertEquals(9, gameBoard.getSquares(player, 3).size());
    }

    @Test
    void getInRangeSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        assertEquals(7, gameBoard.getInRangeSquares(player).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        assertEquals(4, gameBoard.getInRangeSquares(player).size());
        player.setCurrentSquare(gameBoard.getInRangeSquares(player).get(1));
        assertEquals(4, gameBoard.getInRangeSquares(player).size());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        assertEquals(4, gameBoard.getInRangeSquares(player).size());
        player.setCurrentSquare(gameBoard.getInRangeSquares(player).get(2));
        assertEquals(6, gameBoard.getInRangeSquares(player).size());
    }

    @Test
    void getInRangePlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getInRangePlayers(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getInRangePlayers(player).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getInRangePlayers(player).size());
        p2.getCurrentSquare().removePlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(2, gameBoard.getInRangePlayers(player).size());
    }

    @Test
    void getAwayPlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getAwayPlayers(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getAwayPlayers(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getAwayPlayers(player, 1).size());
    }

    @Test
    void getNearPlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getNearPlayers(player, 0).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.getNearPlayers(player, 0).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getNearPlayers(player, 0).size());
    }

    @Test
    void getBetweenPlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getBetweenPlayers(player, 1, 2).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getBetweenPlayers(player, 1, 2).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getBetweenPlayers(player, 1, 2).size());
    }

    @Test
    void getAwaySquares() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getAwaySquares(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getAwaySquares(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getAwaySquares(player, 1).size());
    }

    @Test
    void getNearSquares() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getNearSquares(player, 1).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.getNearSquares(player, 1).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getNearSquares(player, 1).size());
    }

    @Test
    void getBetweenSquares() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getBetweenSquares(player, 1, 2).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getBetweenSquares(player, 1, 2).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 2).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getBetweenSquares(player, 1, 2).size());
    }

    @Test
    void getOtherVisibleRoom() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.getOtherVisibleRoom(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.getOtherVisibleRoom(player).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.getOtherVisibleRoom(player).size());
    }

    @Test
    void getNonVisiblePlayers() {
        Player p2 = new Player("B", YELLOW, gameBoard);
        Player p3 = new Player("C", VIOLET, gameBoard);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        gameBoard.setPlayers(Arrays.asList(player));
        assertEquals(0, gameBoard.getNonVisiblePlayers(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        p2.getCurrentSquare().addPlayer(p2);
        gameBoard.setPlayers(Arrays.asList(player, p2));
        assertEquals(1, gameBoard.getNonVisiblePlayers(player).size());
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p3.getCurrentSquare().addPlayer(p3);
        gameBoard.setPlayers(Arrays.asList(player, p2, p3));
        assertEquals(2, gameBoard.getNonVisiblePlayers(player).size());
    }

    @Test
    void getRoom() {
        assertEquals(2, gameBoard.getRoom(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE)).size());
        assertEquals(2, gameBoard.getRoom(gameBoard.getSpawnAndShopSquare(AmmoColor.RED)).size());
        assertEquals(4, gameBoard.getRoom(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW)).size());
    }
}
