package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.AmmoColor.*;
import static it.polimi.ingsw.model.SquareBorder.*;
import static org.junit.jupiter.api.Assertions.*;

class GrabActionTest {

    private ExtendableAction action;
    private GameBoard gameBoard;
    private Square square2;
    private Player player;

    @BeforeEach
    void setUp() {
        action = new GrabAction();
        gameBoard = new GameBoard(8, 12);
        square2 = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, gameBoard.ammoDeck);
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
        player.setCurrentSquare(square2);
        assertEquals(1, player.getCurrentSquare().getCardsName().size());
        action.doAction();
        assertEquals(1, action.getBranches().size());
        action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
        action.getBranches().get(0).getCompatibleActions().get(0).doAction();
        assertEquals(0, player.getCurrentSquare().getCardsName().size());
    }

    @Test
    void grabAndDrop() {
        player.addWeapon(gameBoard.weaponDeck.draw());
        player.addWeapon(gameBoard.weaponDeck.draw());
        player.addWeapon(gameBoard.weaponDeck.draw());
        action.initialize(player);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        player.getCurrentSquare().addPlayer(player);
        action.doAction();
        assertEquals(3, action.getBranches().size());
        assertEquals(3, player.getCurrentSquare().getCardsName().size());
        action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
        action.getBranches().get(0).getCompatibleActions().get(0).doAction();
        assertEquals(4, player.getLoadedWeapons().size());
        assertEquals(2, player.getCurrentSquare().getCardsName().size());
        action.getBranches().get(0).goNext(action.getBranches().get(0).getCompatibleActions().get(0));
        action.getBranches().get(0).getCompatibleActions().get(0).initialize(player);
        action.getBranches().get(0).getCompatibleActions().get(0).doAction();
        assertEquals(4, player.getLoadedWeapons().size()); // Should be 3
        assertEquals(2, player.getCurrentSquare().getCardsName().size()); // Should be 3
        //TODO Check if discard is correct
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
            action.addDiscarded(action.getDiscardablePowerUps().get(0));
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
