package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.branch.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.SquareBorder.*;
import static org.junit.jupiter.api.Assertions.*;

class GrabActionTest {

    private ExtendableAction action;
    private GameBoard gameBoard;
    private Square ammoSquare;
    private Player player;

    @BeforeEach
    void setUp() {
        action = new GrabAction();
        gameBoard = new GameBoard(8, 3);
        ammoSquare = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, gameBoard.getAmmoDeck());
        player = new Player("A", PlayerColor.GREY, gameBoard);
        player.addBlue(2);
        player.addRed(2);
        player.addYellow(2);
    }

    @Test
    void grabNoDrop() {
        action.initialize(player);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        action.doAction();
        assertEquals(3, action.getBranches().size());
        assertEquals(3, player.getCurrentSquare().getCardsName().size());
        action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
        action.getBranches().get(0).getCompatibleActions().get(0).doAction();
        assertEquals(1, player.getLoadedWeapons().size());
        assertEquals(2, player.getCurrentSquare().getCardsName().size());
        player.getCurrentSquare().refill();
        assertEquals(3, player.getCurrentSquare().getCardsName().size());
        action.getBranches().get(0).goNext(action.getBranches().get(0).getCompatibleActions().get(0));
        assertTrue(action.getBranches().get(0).getCompatibleActions().get(0) instanceof EndBranchAction);
    }

    @Test
    void grabAmmoSquare()
    {
        action.initialize(player);
        player.setCurrentSquare(ammoSquare);
        player.getCurrentSquare().addPlayer(player);
        assertEquals(1, player.getCurrentSquare().getCardsName().size());
        assertEquals(0, action.getPossiblePlayers().size());
        assertEquals(0, action.getPossibleSquares().size());
        assertEquals(0, action.getPossiblePowerUps().size());
        assertEquals(0, action.getDiscardableAmmos().size());
        assertEquals(0, action.getPossibleWeapons().size());
        action.addTarget(player);
        action.addTarget(player.getCurrentSquare());
        action.addWeapon(gameBoard.getWeaponDeck().draw());
        action.use(gameBoard.getPowerupDeck().draw());
        action.discard(new Ammo(0, 0, 0));
        action.doAction();
        assertEquals(1, action.getBranches().size());
        action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
        action.getBranches().get(0).getCompatibleActions().get(0).doAction();
        assertEquals(0, player.getCurrentSquare().getCardsName().size());
        assertEquals(0, action.getPossiblePlayers().size());
        assertEquals(0, action.getPossibleSquares().size());
        assertEquals(0, action.getPossiblePowerUps().size());
        assertEquals(0, action.getDiscardableAmmos().size());
        assertEquals(0, action.getPossibleWeapons().size());
        player.getCurrentSquare().addWeapon(gameBoard.getWeaponDeck().draw());
        player.getCurrentSquare().removeWeapon(gameBoard.getWeaponDeck().draw());
        assertEquals(0, player.getCurrentSquare().getWeapons().size());
    }

    @Test
    void grabAndDrop() {
        Action action;
        player.addWeapon(gameBoard.getWeaponDeck().draw());
        player.addWeapon(gameBoard.getWeaponDeck().draw());
        player.addWeapon(gameBoard.getWeaponDeck().draw());
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        Branch branch = new Branch(new GrabAction(), new EndBranchAction());
        action = branch.getCompatibleActions().get(0);
        assertTrue(action instanceof GrabAction);
        action.initialize(player);
        action.doAction();
        assertEquals(3, player.getLoadedWeapons().size());
        assertEquals(3, player.getCurrentSquare().getCardsName().size());
        assertEquals(3, ((ExtendableAction)action).getBranches().size());
        branch = ((ExtendableAction)action).getBranches().get(0);
        action = branch.getCompatibleActions().get(0);
        action.initialize(player);
        action.doAction();
        assertEquals(4, player.getLoadedWeapons().size());
        assertEquals(2, player.getCurrentSquare().getCardsName().size());
        branch.goNext(action);
        action = branch.getCompatibleActions().get(0);
        action.initialize(player);
        action.doAction();
        assertEquals(3, ((ExtendableAction)action).getBranches().size());
        branch = ((ExtendableAction)action).getBranches().get(0);
        action = branch.getCompatibleActions().get(0);
        action.initialize(player);
        action.doAction();
        assertEquals(3, player.getLoadedWeapons().size());
        assertEquals(3, player.getCurrentSquare().getCardsName().size());
    }

    @Test
    void getDiscardablePowerUps() {
        action.initialize(player);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.addPowerUpCard();
        player.addPowerUpCard();
        player.addPowerUpCard();
        player.addPowerUpCardRespawn();
        if(action.getDiscardablePowerUps().size() >= 1) {
            assertTrue(action.getDiscardablePowerUps().size() >= 1 && action.getDiscardablePowerUps().size() <= 4);
            action.addPowerUp(action.getDiscardablePowerUps().get(0));
            assertTrue(action.getDiscardablePowerUps().size() < 4);
            action.doAction();
            action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
            action.getBranches().get(0).getCompatibleActions().get(0).doAction();
            assertEquals(1, player.getLoadedWeapons().size());
            assertEquals(3, player.getPowerUps().size());
        }
        else {
            assertEquals(0, player.getLoadedWeapons().size());
            assertEquals(0, action.getDiscardablePowerUps().size());
        }
    }
}
