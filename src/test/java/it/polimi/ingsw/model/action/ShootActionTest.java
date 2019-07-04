package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.WeaponCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShootActionTest {

    private GameBoard gameBoard;
    private Player p1;
    private Player p2;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(5, 0);
        p1 = new Player("A", PlayerColor.BLUE, gameBoard);
        p2 = new Player("B", PlayerColor.GREEN, gameBoard);
    }

    @Test
    void shootDamage()
    {
        WeaponCard machineGun = CardsFactory.getWeapons().get(1);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(machineGun);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = machineGun.getFireModalitiesBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        assertFalse(shootAction.canBeDone());
        shootAction.addTarget(p2);
        assertTrue(shootAction.canBeDone());
        assertEquals(0, shootAction.getDiscardablePowerUps().size());
        shootAction.addPowerUp(gameBoard.powerupDeck.draw());
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
        assertEquals(0, shootAction.getDiscardablePowerUps().size());
    }

    @Test
    void shootDamageAll()
    {
        WeaponCard electroschyte = CardsFactory.getWeapons().get(5);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(electroschyte);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = electroschyte.getFireModalitiesBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        shootAction.addTarget(p2.getCurrentSquare());
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
    }

    @Test
    void shootMoveAndCanBeDone()
    {
        WeaponCard grenadeLauncher = CardsFactory.getWeapons().get(12);
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(grenadeLauncher);
        p2.setCurrentSquare(gameBoard.getSquares().get(4));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = grenadeLauncher.getFireModalitiesBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        shootAction.doAction();
        assertEquals(0, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(4), p2.getCurrentSquare());
        shootAction.addTarget(p2);
        shootAction.doAction();
        assertEquals(0, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(4), p2.getCurrentSquare());
        shootAction.addTarget(p1.gameBoard.getSquares().get(5));
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(5), p2.getCurrentSquare());
    }
}
