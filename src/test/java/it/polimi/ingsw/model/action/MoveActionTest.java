package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MoveActionTest {
    GameBoard gameBoard = new GameBoard(3, 10);
    private ArrayList<MoveAction> moveActions = new ArrayList<>();
    private Action triggeredAction = null;
    private boolean eventTriggered = false;

    @Test
    void isCompatible()
    {
        final int n = 20;
        for(int i=0; i < n; i++)
            moveActions.add(new MoveAction(new Player("prova", PlayerColor.BLUE, gameBoard), i)); // Mi (MoveAction with MaxDist i)

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
    void actionCompletedEventTest()
    {
        eventTriggered=false;
        Action action = new MoveAction(new Player("c", PlayerColor.BLUE, gameBoard), 3);
        action.completedActionEvent.addEventHandler((a,b)->this.eventTriggered = true);
        action.doAction();
        assertTrue(eventTriggered);

        eventTriggered = false;
        triggeredAction = new MoveAction(new Player("c", PlayerColor.BLUE, gameBoard), 3);
        triggeredAction.completedActionEvent.addEventHandler(this::eventHandler);
        triggeredAction.doAction();
        assertTrue(eventTriggered);
    }

    void eventHandler(Action sender, Action completedAction)
    {
        assertTrue(completedAction == triggeredAction);
        eventTriggered= true;
    }
}