package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShootActionTest {

    private GameBoard gameBoard = new GameBoard(5, 10);
    private Player p1 = new Player("p", PlayerColor.BLUE, gameBoard);
    private Player p2 = new Player("p", PlayerColor.BLUE, gameBoard);
    private WeaponCard machineGun = WeaponFactory.getWeapons().get(1);
    private WeaponCard electroschyte = WeaponFactory.getWeapons().get(5);
    private WeaponCard grenadeLauncher = WeaponFactory.getWeapons().get(12);

    @Test
    void shoot1()
    {
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(machineGun);
        p2.setCurrentSquare(gameBoard.getSquares().get(5));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = machineGun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        shootAction.addTarget(p2);
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
    }

    @Test
    void shoot2()
    {
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(electroschyte);
        p2.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = electroschyte.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        shootAction.addTarget(p2.getCurrentSquare());
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
    }

    @Test
    void shoot3()
    {
        p1.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p1.getCurrentSquare().addPlayer(p1);
        p1.addWeapon(grenadeLauncher);
        p2.setCurrentSquare(gameBoard.getSquares().get(4));
        p2.getCurrentSquare().addPlayer(p2);
        Action shootAction = grenadeLauncher.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shootAction.initialize(p1);
        shootAction.addTarget(p2);
        shootAction.addTarget(p1.gameBoard.getSquares().get(5));
        shootAction.doAction();
        assertEquals(1, p2.getDamage().size());
        assertEquals(p1.gameBoard.getSquares().get(5), p2.getCurrentSquare());
    }
}
