package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.model.AmmoColor.RED;
import static it.polimi.ingsw.model.AmmoColor.YELLOW;
import static it.polimi.ingsw.model.PlayerColor.BLUE;
import static it.polimi.ingsw.model.PlayerColor.GREY;
import static it.polimi.ingsw.model.PlayerColor.VIOLET;
import static org.junit.jupiter.api.Assertions.*;

class EffectsTest {

    GameBoard gameBoard = new GameBoard(8, 10);
    Player p1;
    Player p2;
    Player p3;

    @BeforeEach
    void setUp() {
        p1 = new Player("A", BLUE, gameBoard);
        p2 = new Player("B", GREY, gameBoard);
        p3 = new Player("C", VIOLET, gameBoard);
    }

    @Test
    void damage() {
        Effects.damage(p1, new ArrayList<>(Arrays.asList(p2, p3)), new ArrayList<>(Arrays.asList(1, 2)));
        assertEquals(1, p2.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(0));
        assertEquals(2, p3.getDamage().size());
        assertEquals(BLUE, p3.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(1));
    }

    @Test
    void mark() {
        Effects.mark(p1, new ArrayList<>(Arrays.asList(p2, p3)), new ArrayList<>(Arrays.asList(1, 2)));
        assertEquals(1, p2.getMark().size());
        assertEquals(BLUE, p2.getMark().get(0));
        assertEquals(2, p3.getMark().size());
        assertEquals(BLUE, p3.getMark().get(0));
        assertEquals(BLUE, p3.getMark().get(1));
    }

    @Test
    void damageAll() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        Effects.damageAll(p1, new ArrayList<>(Arrays.asList(gameBoard.getSpawnAndShopSquare(RED), gameBoard.getSpawnAndShopSquare(YELLOW))), new ArrayList<>(Arrays.asList(1, 2)));
        assertEquals(1, p2.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(0));
        assertEquals(2, p3.getDamage().size());
        assertEquals(BLUE, p3.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(1));
    }

    @Test
    void markAll() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        Effects.markAll(p1, new ArrayList<>(Arrays.asList(gameBoard.getSpawnAndShopSquare(RED), gameBoard.getSpawnAndShopSquare(YELLOW))), new ArrayList<>(Arrays.asList(1, 2)));
        assertEquals(1, p2.getMark().size());
        assertEquals(BLUE, p2.getMark().get(0));
        assertEquals(2, p3.getMark().size());
        assertEquals(BLUE, p3.getMark().get(0));
        assertEquals(BLUE, p3.getMark().get(1));
    }
}