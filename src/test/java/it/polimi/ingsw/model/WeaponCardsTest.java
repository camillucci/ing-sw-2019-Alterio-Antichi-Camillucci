package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.action.ShootAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class WeaponCardsTest {

    private GameBoard gameBoard;
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;
    private Player p5;
    private Action shoot;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(8, 12);
        p1 = new Player("A", BLUE, gameBoard);
        p2 = new Player("B", GREEN, gameBoard);
        p3 = new Player("C", GREY, gameBoard);
        p4 = new Player("D", VIOLET, gameBoard);
        p5 = new Player("E", YELLOW, gameBoard);
        gameBoard.setPlayers(Arrays.asList(p1, p2, p3, p4, p5));
        p1.setCurrentSquare(gameBoard.getSquares().get(4));
        p1.getCurrentSquare().addPlayer(p1);
        p2.setCurrentSquare(gameBoard.getSquares().get(9));
        p2.getCurrentSquare().addPlayer(p2);
        p3.setCurrentSquare(gameBoard.getSquares().get(6));
        p3.getCurrentSquare().addPlayer(p3);
        p4.setCurrentSquare(gameBoard.getSquares().get(6));
        p4.getCurrentSquare().addPlayer(p4);
        p5.setCurrentSquare(gameBoard.getSquares().get(2));
        p5.getCurrentSquare().addPlayer(p5);
    }

    @Test
    void lockRifle() {
        WeaponCard lockRifle = CardsFactory.getWeapons().get(0);
        shoot = lockRifle.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0); // Fist FireModality -> First Branch -> First Action
        shoot.initialize(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(1, p3.getMark().size());
    }

    @Test
    void machineGun() {
        WeaponCard machineGun = CardsFactory.getWeapons().get(1);
        shoot = machineGun.getFireModalitysBranch(3).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
    }

    @Test
    void thor() {
        WeaponCard thor = CardsFactory.getWeapons().get(2);
        shoot = thor.getFireModalitysBranch(2).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p2.getDamage().size());
        assertEquals(1, p3.getDamage().size());
        assertEquals(2, p4.getDamage().size());
    }

    @Test
    void plasmaGun() {
        WeaponCard plasmaGun = CardsFactory.getWeapons().get(3);
        assertTrue(plasmaGun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0) instanceof MoveAction);
        assertTrue(plasmaGun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(1) instanceof ShootAction);
        shoot = plasmaGun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(1);
        shoot.initialize(p1);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p2);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p2.getDamage().size());
        assertEquals(0, p2.getMark().size());
    }

    @Test
    void whisper() {
        WeaponCard whisper = CardsFactory.getWeapons().get(4);
        shoot = whisper.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.initialize(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p4.getDamage().size());
        assertEquals(1, p4.getMark().size());
    }

    @Test
    void electroscythe() {
        WeaponCard electroscythe = CardsFactory.getWeapons().get(5);
        shoot = electroscythe.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(p4.getCurrentSquare());
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
    }

    @Test
    void tractorBeam1() {
        WeaponCard tractorBeam = CardsFactory.getWeapons().get(6);
        shoot = tractorBeam.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(5, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(10));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(10), p3.getCurrentSquare());
    }

    @Test
    void tractorBeam2() {
        WeaponCard tractorBeam = CardsFactory.getWeapons().get(6);
        shoot = tractorBeam.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(9));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(9), p3.getCurrentSquare());
    }
}
