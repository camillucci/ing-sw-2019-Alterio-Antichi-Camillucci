package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.model.AmmoColor.RED;
import static it.polimi.ingsw.model.AmmoColor.YELLOW;
import static it.polimi.ingsw.model.PlayerColor.BLUE;
import static it.polimi.ingsw.model.PlayerColor.GREY;
import static it.polimi.ingsw.model.PlayerColor.VIOLET;
import static org.junit.jupiter.api.Assertions.*;

class EffectsTest {

    private GameBoard gameBoard = new GameBoard(8, 10);
    private Player p1;
    private Player p2;
    private Player p3;

    @BeforeEach
    void setUp() {
        p1 = new Player("A", BLUE, gameBoard);
        p2 = new Player("B", GREY, gameBoard);
        p3 = new Player("C", VIOLET, gameBoard);
    }

    @Test
    void damage() {
        Effects.damage(p1, Arrays.asList(p2, p3), Arrays.asList(1, 2));
        assertEquals(1, p2.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(0));
        assertEquals(2, p3.getDamage().size());
        assertEquals(BLUE, p3.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(1));
    }

    @Test
    void mark() {
        Effects.mark(p1, Arrays.asList(p2, p3), Arrays.asList(1, 2));
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
        Effects.damageAll(p1, Arrays.asList(gameBoard.getSpawnAndShopSquare(RED), gameBoard.getSpawnAndShopSquare(YELLOW)), Arrays.asList(1, 2));
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
        Effects.markAll(p1, Arrays.asList(gameBoard.getSpawnAndShopSquare(RED), gameBoard.getSpawnAndShopSquare(YELLOW)), Arrays.asList(1, 2));
        assertEquals(1, p2.getMark().size());
        assertEquals(BLUE, p2.getMark().get(0));
        assertEquals(2, p3.getMark().size());
        assertEquals(BLUE, p3.getMark().get(0));
        assertEquals(BLUE, p3.getMark().get(1));
    }

    @Test
    void damageRoom() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        Effects.damageRoom(p1, Collections.singletonList(gameBoard.getSpawnAndShopSquare(YELLOW)), Collections.singletonList(2));
        assertEquals(0, p2.getDamage().size());
        assertEquals(2, p3.getDamage().size());
        assertEquals(BLUE, p3.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(1));
    }

    @Test
    void damageMultiple() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(RED));
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(YELLOW));
        Effects.damageMultiple(p1, Arrays.asList(p2, p3), Arrays.asList(1, 2, 1, 2));
        assertEquals(3, p2.getDamage().size());
        assertEquals(3, p3.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(2));
        assertEquals(BLUE, p3.getDamage().get(2));
        assertEquals(3, p2.getMark().size());
        assertEquals(3, p3.getMark().size());
        assertEquals(BLUE, p2.getMark().get(1));
        assertEquals(BLUE, p3.getMark().get(1));
    }

    @Test
    void move() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(RED));
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(YELLOW));
        Effects.move(Arrays.asList(p2, p3), Collections.singletonList(gameBoard.getSpawnAndShopSquare(YELLOW)));
        assertEquals(gameBoard.getSpawnAndShopSquare(YELLOW), p2.getCurrentSquare());
        assertEquals(gameBoard.getSpawnAndShopSquare(YELLOW), p3.getCurrentSquare());
    }

    /*
    @Test
    void moveAndDamage() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(RED));
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(YELLOW));
        Effects.moveAndDamage(p1, Arrays.asList(p2, p3), Arrays.asList(gameBoard.getSpawnAndShopSquare(YELLOW), gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE)), Arrays.asList(1, 2));
        assertEquals(1, p2.getDamage().size());
        assertEquals(2, p3.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(1));
        assertEquals(gameBoard.getSpawnAndShopSquare(YELLOW), p2.getCurrentSquare());
        assertEquals(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE), p3.getCurrentSquare());
    }

    @Test
    void moveAndMultipleDamage() {
        gameBoard.getSpawnAndShopSquare(RED).addPlayer(p2);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(RED));
        gameBoard.getSpawnAndShopSquare(YELLOW).addPlayer(p3);
        p3.setCurrentSquare(gameBoard.getSpawnAndShopSquare(YELLOW));
        Effects.moveAndMultipleDamage(p1, Arrays.asList(p2, p3), Arrays.asList(gameBoard.getSpawnAndShopSquare(YELLOW), gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE)), Arrays.asList(1, 0, 0, 0));
        assertEquals(1, p2.getDamage().size());
        assertEquals(1, p3.getDamage().size());
        assertEquals(BLUE, p2.getDamage().get(0));
        assertEquals(BLUE, p3.getDamage().get(0));
        assertEquals(gameBoard.getSpawnAndShopSquare(YELLOW), p2.getCurrentSquare());
        assertEquals(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE), p3.getCurrentSquare());
    }
    */
}
