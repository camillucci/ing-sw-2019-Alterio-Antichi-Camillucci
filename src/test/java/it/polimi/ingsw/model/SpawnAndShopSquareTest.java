package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SpawnAndShopSquareTest {

    private GameBoard gameBoard = new GameBoard(7, 11);
    private Player player = new Player("A", PlayerColor.GREY, gameBoard);

    @Test
    void grab() {
        player.addBlue(2);
        player.addRed(2);
        player.addYellow(2);
        assertEquals(3, gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE).grab(player, Collections.emptyList()).size());
        player.addWeapon(null);
        player.addWeapon(null);
        player.addWeapon(null);
        assertEquals(3, gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE).grab(player, Collections.emptyList()).size());
    }
}