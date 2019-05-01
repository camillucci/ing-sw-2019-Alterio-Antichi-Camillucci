package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private GameBoard gameBoard = new GameBoard(gameLength, 12);
    private Player player = new Player("A", GREY, gameBoard);
    private Player p2;
    private Player p3;
    private static final int gameLength = 6;
    private static final int maxKillShotTrack = 8;

    @BeforeEach
    void setUp() {
        p2 = new Player("B", YELLOW, gameBoard);
        p3 = new Player("C", VIOLET, gameBoard);
    }

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
    void distanceOneSquares() {
        assertEquals(4, gameBoard.distanceOneSquares(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE)).size());
        assertEquals(3, gameBoard.distanceOneSquares(gameBoard.getSpawnAndShopSquare(AmmoColor.RED)).size());
        assertEquals(3, gameBoard.distanceOneSquares(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW)).size());
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
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.removeNonPlayerSquares(player, gameBoard.getAwaySquares(player, 1)).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.removeNonPlayerSquares(player, gameBoard.getAwaySquares(player, 1)).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.removeNonPlayerSquares(player, gameBoard.getAwaySquares(player, 1)).size());
    }

    @Test
    void getNearSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.removeNonPlayerSquares(player, gameBoard.getNearSquares(player, 1)).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.removeNonPlayerSquares(player, gameBoard.getNearSquares(player, 1)).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 3).get(4));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.removeNonPlayerSquares(player, gameBoard.getNearSquares(player, 1)).size());
    }

    @Test
    void getBetweenSquares() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.removeNonPlayerSquares(player, gameBoard.getBetweenSquares(player, 1, 2)).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(0, gameBoard.removeNonPlayerSquares(player, gameBoard.getBetweenSquares(player, 1, 2)).size());
        p3.setCurrentSquare(gameBoard.getSquares(player, 2).get(2));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(1, gameBoard.removeNonPlayerSquares(player, gameBoard.getBetweenSquares(player, 1, 2)).size());
    }

    @Test
    void getOtherVisibleRoom() {
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
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        gameBoard.setPlayers(Collections.singletonList(player));
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

    @Test
    void sameDirectionAndSameDirectionSquare1() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.sameDirection(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.setCurrentSquare(gameBoard.getSquares(p2, 1).get(2));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p2.getCurrentSquare()).size());
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p3.setCurrentSquare(gameBoard.getSquares(p3, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(gameBoard.getSquares(p2, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(1, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(player.getCurrentSquare());
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
    }

    @Test
    void sameDirectionAndSameDirectionSquare2() {
        player.setCurrentSquare(gameBoard.getSquares().get(9));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.sameDirection(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p2.getCurrentSquare()).size());
        p3.setCurrentSquare(gameBoard.getSquares().get(5));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(gameBoard.getSquares().get(10));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(1, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
    }

    @Test
    void sameDirectionAndSameDirectionSquare3() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.sameDirection(player).size());
        p2.setCurrentSquare(gameBoard.getSquares().get(9));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.sameDirection(player).size());
        assertEquals(0, gameBoard.sameDirectionSquare(player, p2.getCurrentSquare()).size());
        p3.setCurrentSquare(gameBoard.getSquares().get(10));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.sameDirection(player).size());
        assertEquals(1, gameBoard.sameDirectionSquare(player, p2.getCurrentSquare()).size());
        assertEquals(1, gameBoard.sameDirectionSquare(player, p3.getCurrentSquare()).size());
    }

    @Test
    void throughWalls1() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.throughWalls(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.setCurrentSquare(gameBoard.getSquares(p2, 1).get(2));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.throughWalls(player).size());
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p3.setCurrentSquare(gameBoard.getSquares(p3, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.throughWalls(player).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(gameBoard.getSquares(p2, 1).get(1));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.throughWalls(player).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(player.getCurrentSquare());
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.throughWalls(player).size());
    }

    @Test
    void throughWalls2() {
        player.setCurrentSquare(gameBoard.getSquares().get(9));
        player.getCurrentSquare().addPlayer(player);
        assertEquals(0, gameBoard.throughWalls(player).size());
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.YELLOW));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.throughWalls(player).size());
        p3.setCurrentSquare(gameBoard.getSquares().get(5));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.throughWalls(player).size());
        p3.getCurrentSquare().removePlayer(p3);
        p3.setCurrentSquare(gameBoard.getSquares().get(10));
        p3.getCurrentSquare().addPlayer(p3);
        assertEquals(2, gameBoard.throughWalls(player).size());
    }
    @Test
    void throughWalls3() {
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        player.getCurrentSquare().addPlayer(player);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        assertEquals(1, gameBoard.throughWalls(player).size());
    }

    @Test
    void squares2() {
        List<Square> temp = gameBoard.getSquares();
        assertTrue(temp.get(0) instanceof AmmoSquare);
        assertTrue(temp.get(1) instanceof AmmoSquare);
        assertTrue(temp.get(2) instanceof SpawnAndShopSquare);
        assertTrue(temp.get(3) instanceof AmmoSquare);
        assertTrue(temp.get(4) instanceof SpawnAndShopSquare);
        assertTrue(temp.get(5) instanceof AmmoSquare);
        assertTrue(temp.get(6) instanceof AmmoSquare);
        assertTrue(temp.get(7) instanceof AmmoSquare);
        assertTrue(temp.get(8) instanceof AmmoSquare);
        assertTrue(temp.get(9) instanceof AmmoSquare);
        assertTrue(temp.get(10) instanceof AmmoSquare);
        assertTrue(temp.get(11) instanceof SpawnAndShopSquare);
    }
}
