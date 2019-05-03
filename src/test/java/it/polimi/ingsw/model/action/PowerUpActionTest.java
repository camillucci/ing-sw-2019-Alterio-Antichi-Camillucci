package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpActionTest {

    private GameBoard gameBoard = new GameBoard(5, 10);
    private Player p1 = new Player("p", PlayerColor.BLUE, gameBoard);
    private Player p2 = new Player("p", PlayerColor.BLUE, gameBoard);
    private PowerUpCard newton = CardsFactory.getPowerUps().get(1);
    private PowerUpCard teleporter = CardsFactory.getPowerUps().get(3);

    @Test
    void shoot1()
    {
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        gameBoard.setPlayers(Arrays.asList(p1, p2));
        Action powerUpAction = newton.getEffect();
        powerUpAction.initialize(p1);
        powerUpAction.addTarget(p2);
        powerUpAction.addTarget(gameBoard.getSquares().get(4));
        powerUpAction.doAction();
        assertEquals(0, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(4), p2.getCurrentSquare());
    }

    @Test
    void shoot2()
    {
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        Action powerUpAction = teleporter.getEffect();
        powerUpAction.initialize(p1);
        powerUpAction.addTarget(p1);
        powerUpAction.addTarget(gameBoard.getSquares().get(6));
        powerUpAction.doAction();
        assertEquals(p1.gameBoard.getSquares().get(6), p1.getCurrentSquare());
    }

    @Test
    void shoot3()
    {

    }
}
