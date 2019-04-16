package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.SquareBorder.*;
import static org.junit.jupiter.api.Assertions.*;

class GrabActionTest {

    private ExtendableAction action = new GrabAction();
    private GameBoard gameBoard = new GameBoard(8, 12);
    private Square square2 = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, new AmmoCard(1, 1, 1, true));
    private Player player = new Player("A", PlayerColor.GREY, gameBoard);

    @Test
    void op() {
        action.initialize(player);
        player.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.BLUE));
        action.doAction();
        assertEquals(3, action.getBranches().size());
        player.setCurrentSquare(square2);
        action.doAction();
        assertEquals(0, action.getBranches().size());
    }
}
