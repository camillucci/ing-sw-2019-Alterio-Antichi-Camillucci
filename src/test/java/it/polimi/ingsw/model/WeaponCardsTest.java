package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.action.ShootAction;
import it.polimi.ingsw.model.cards.CardsFactory;
import it.polimi.ingsw.model.cards.WeaponCard;
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
        gameBoard = new GameBoard(8, 3);
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
        shoot.addTarget(p1);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(0));
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
        shoot.addTarget(p1);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(0));
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

    @Test
    void vortexCannon1() {
        WeaponCard vortexCannon = CardsFactory.getWeapons().get(7);
        shoot = vortexCannon.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(5, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(7));
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(7), p3.getCurrentSquare());
        assertEquals(0, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p4.getCurrentSquare());
    }

    @Test
    void vortexCannon2() {
        WeaponCard vortexCannon = CardsFactory.getWeapons().get(7);
        shoot = vortexCannon.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(5, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(6));
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p3.getCurrentSquare());
        assertEquals(1, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p5.getCurrentSquare());
        assertEquals(0, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p4.getCurrentSquare());
    }

    @Test
    void furnace1() {
        WeaponCard furnace = CardsFactory.getWeapons().get(8);
        shoot = furnace.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(10));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(1, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
    }

    @Test
    void furnace2() {
        WeaponCard furnace = CardsFactory.getWeapons().get(8);
        shoot = furnace.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(6));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(1, p3.getMark().size());
        assertEquals(1, p4.getDamage().size());
        assertEquals(1, p4.getMark().size());
    }

    @Test
    void heatseeker() {
        WeaponCard heatseeker = CardsFactory.getWeapons().get(9);
        shoot = heatseeker.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
    }

    @Test
    void hellion() {
        WeaponCard hellion = CardsFactory.getWeapons().get(10);
        shoot = hellion.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p3);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.initialize(p5);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p4.getDamage().size());
        assertEquals(1, p4.getMark().size());
        assertEquals(0, p3.getDamage().size());
        assertEquals(1, p3.getMark().size());
    }

    @Test
    void flamethrower1() {
        WeaponCard flamethrower = CardsFactory.getWeapons().get(11);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(10));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = flamethrower.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(1, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
    }

    @Test
    void flamethrower2() {
        WeaponCard flamethrower = CardsFactory.getWeapons().get(11);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(10));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = flamethrower.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(2, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(6));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(2));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(2, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(1, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
    }

    @Test
    void flamethrower3() {
        WeaponCard flamethrower = CardsFactory.getWeapons().get(11);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(0));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = flamethrower.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(2, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(1));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(0));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p1.getDamage().size());
        assertEquals(0, p1.getMark().size());
    }

    @Test
    void grenadeLauncher1() {
        WeaponCard grenadeLauncher = CardsFactory.getWeapons().get(12);
        shoot = grenadeLauncher.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(4, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(7));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(7), p3.getCurrentSquare());
        assertEquals(0, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p4.getCurrentSquare());
    }

    @Test
    void grenadeLauncher2() {
        WeaponCard grenadeLauncher = CardsFactory.getWeapons().get(12);
        shoot = grenadeLauncher.getFireModalitysBranch(1).get(1).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(1, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(6));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(1, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
    }

    @Test
    void rocketLauncher() {
        WeaponCard rocketLauncher = CardsFactory.getWeapons().get(13);
        shoot = rocketLauncher.getFireModalitysBranch(3).get(0).getCompatibleActions().get(0);
        shoot.initialize(p2);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(4, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(7));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(7), p3.getCurrentSquare());
        assertEquals(1, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p4.getCurrentSquare());
    }

    @Test
    void railgun() {
        WeaponCard railgun = CardsFactory.getWeapons().get(14);
        shoot = railgun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p3);
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p1);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot = railgun.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p3);
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p1);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p1.getDamage().size());
        assertEquals(0, p1.getMark().size());
        assertEquals(2, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
    }

    @Test
    void cyberblade() {
        WeaponCard cyberblade = CardsFactory.getWeapons().get(15);
        shoot = cyberblade.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p4);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
    }

    @Test
    void zx2() {
        WeaponCard zx2 = CardsFactory.getWeapons().get(16);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(3));
        p1.getCurrentSquare().addPlayer(p1);
        p2.getCurrentSquare().removePlayer(p2);
        p2.setCurrentSquare(gameBoard.getSquares().get(11));
        p2.getCurrentSquare().addPlayer(p2);
        shoot = zx2.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(3, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(0, p3.getDamage().size());
        assertEquals(1, p3.getMark().size());
        assertEquals(0, p4.getDamage().size());
        assertEquals(1, p4.getMark().size());
        assertEquals(0, p5.getDamage().size());
        assertEquals(1, p5.getMark().size());
    }

    @Test
    void shotgun() {
        WeaponCard shotgun = CardsFactory.getWeapons().get(17);
        shoot = shotgun.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p4);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        assertEquals(p5, shoot.getPossiblePlayers().get(0));
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot = shotgun.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p4);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        assertEquals(p3, shoot.getPossiblePlayers().get(0));
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(4, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(7));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(7), p3.getCurrentSquare());
    }

    @Test
    void powerGlove1() {
        WeaponCard powerGlove = CardsFactory.getWeapons().get(18);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(10));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = powerGlove.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
        assertEquals(gameBoard.getSquares().get(2), p5.getCurrentSquare());
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p3.getCurrentSquare());
        assertEquals(gameBoard.getSquares().get(2), p1.getCurrentSquare());
    }

    @Test
    void powerGlove2() {
        WeaponCard powerGlove = CardsFactory.getWeapons().get(18);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(10));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = powerGlove.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p3);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p3.getDamage().size());
        assertEquals(0, p3.getMark().size());
        assertEquals(gameBoard.getSquares().get(6), p3.getCurrentSquare());
        assertEquals(2, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
        assertEquals(gameBoard.getSquares().get(2), p5.getCurrentSquare());
        assertEquals(gameBoard.getSquares().get(2), p1.getCurrentSquare());
    }

    @Test
    void powerGlove3() {
        WeaponCard powerGlove = CardsFactory.getWeapons().get(18);
        p1.getCurrentSquare().removePlayer(p1);
        p1.setCurrentSquare(gameBoard.getSquares().get(10));
        p1.getCurrentSquare().addPlayer(p1);
        shoot = powerGlove.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p1);
        assertEquals(4, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(2, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(2, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
        assertEquals(gameBoard.getSquares().get(2), p5.getCurrentSquare());
        assertEquals(gameBoard.getSquares().get(2), p1.getCurrentSquare());
    }

    @Test
    void shockwave() {
        WeaponCard shockwave = CardsFactory.getWeapons().get(19);
        shoot = shockwave.getFireModalitysBranch(0).get(0).getCompatibleActions().get(0);
        shoot.initialize(p4);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p5);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(1, p5.getDamage().size());
        assertEquals(0, p5.getMark().size());
    }

    @Test
    void sledgehammer() {
        WeaponCard sledgehammer = CardsFactory.getWeapons().get(20);
        shoot = sledgehammer.getFireModalitysBranch(1).get(0).getCompatibleActions().get(0);
        shoot.initialize(p3);
        assertEquals(1, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.addTarget(p4);
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(4, shoot.getPossibleSquares().size());
        shoot.addTarget(gameBoard.getSquares().get(10));
        assertEquals(0, shoot.getPossiblePlayers().size());
        assertEquals(0, shoot.getPossibleSquares().size());
        shoot.doAction();
        assertEquals(3, p4.getDamage().size());
        assertEquals(0, p4.getMark().size());
        assertEquals(gameBoard.getSquares().get(10), p4.getCurrentSquare());
    }
}
