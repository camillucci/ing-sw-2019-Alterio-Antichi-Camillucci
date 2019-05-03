package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {
    private GameBoard gameBoard = new GameBoard(3, 10);
    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private Action triggeredAction = null;
    private boolean eventTriggered = false;

    @Test
    void isCompatible1()
    {
        final int n = 20;
        for(int i=0; i < n; i++)
            moveActions.add(new MoveAction(i)); // Mi (MoveAction with MaxDist i)

        for(int i = 0; i < n-1; i++)
            for(int j= i+1; j < n; j++)
            {
                // i < j => Mj not compatible with Mi && Mi compatible with Mj
                assertTrue(moveActions.get(j).isCompatible(moveActions.get(i)));
                assertFalse(moveActions.get(i).isCompatible(moveActions.get(j)));
            }
        for(int i=0; i < n; i++)
            assertTrue(moveActions.get(i).isCompatible(moveActions.get(i))); // Mi compatible with Mi
    }

    @Test
    void isCompatible2()
    {
        MoveAction a1 = new MoveAction(0, 7);
        MoveAction a2 = new MoveAction(2, 5);
        assertTrue(a1.isCompatible(a2));
        assertFalse(a2.isCompatible(a1));
    }

    @Test
    void actionCompletedEventTest1()
    {
        Player p = new Player("name", PlayerColor.BLUE, gameBoard);
        p.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p.getCurrentSquare().addPlayer(p);
        eventTriggered = false;
        Action action = new MoveAction(3);
        action.completedActionEvent.addEventHandler((a,b)->this.eventTriggered = true);
        action.initialize(p);
        assertEquals(9, action.getPossibleSquares().size());
        action.addTarget(gameBoard.getSquares().get(2));
        action.doAction();
        assertTrue(eventTriggered);
        assertEquals(gameBoard.getSquares().get(2), p.getCurrentSquare());

        eventTriggered = false;
        triggeredAction = new MoveAction(3);
        triggeredAction.completedActionEvent.addEventHandler(this::eventHandler);
        action.initialize(p);
        action.doAction();
        assertTrue(eventTriggered);
    }

    @Test
    void actionCompletedEventTest2()
    {
        Player p = new Player("name", PlayerColor.BLUE, gameBoard);
        p.setCurrentSquare(gameBoard.getSpawnAndShopSquare(AmmoColor.RED));
        p.getCurrentSquare().addPlayer(p);
        eventTriggered = false;
        Action action = new MoveAction(1, 3);
        action.completedActionEvent.addEventHandler((a,b)->this.eventTriggered = true);
        action.initialize(p);
        assertEquals(8, action.getPossibleSquares().size());
        action.addTarget(gameBoard.getSquares().get(1));
        action.doAction();
        assertTrue(eventTriggered);
        assertEquals(gameBoard.getSquares().get(1), p.getCurrentSquare());

        eventTriggered = false;
        triggeredAction = new MoveAction(1, 3);
        triggeredAction.completedActionEvent.addEventHandler(this::eventHandler);
        action.initialize(p);
        action.doAction();
        assertTrue(eventTriggered);
    }

    void eventHandler(Action sender, Action completedAction)
    {
        assertSame(completedAction, triggeredAction);
        eventTriggered= true;
    }
}
