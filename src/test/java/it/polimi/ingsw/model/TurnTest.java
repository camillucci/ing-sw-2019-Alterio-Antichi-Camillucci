package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.MoveAction;
import it.polimi.ingsw.model.action.RollBackAction;
import it.polimi.ingsw.model.branch.BranchTestUtilities;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnTest
{
    private boolean endTurn = false;
    List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
    List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
    Match match = new Match(names, colors, 8, 10);
    Bot bot = new Bot();
    boolean triggered = false;

    @Test
    void test1()
    {
        Turn turn = new Turn(new Player("a", BLUE, new GameBoard(10,7)), match);
        turn.endTurnEvent.addEventHandler((a,b) -> endTurn = true);
        assertFalse(endTurn);

        bot.playNoAdrenaline(turn); // shooter damaged by 3 or 4
        assertFalse(endTurn);
        bot.playThreeDamage(turn);
        assertTrue(endTurn);
    }

    @Test
    void test2() // rollback in Turn
    {
        Turn turn = new Turn(new Player("a", BLUE, new GameBoard(10,7)), match);
        turn.endTurnEvent.addEventHandler((a,b) -> triggered = true);

        bot.playNoAdrenaline(turn); // shooter damaged by 3 or 4

        for(int i = 0, N = 200; i < N; i++)
        {
            BranchTestUtilities.searchAndDo(turn.getActions(), new MoveAction(2));
            BranchTestUtilities.searchAndDo(turn.getActions(), new RollBackAction());
            assertFalse(triggered);
        }

        bot.playThreeDamage(turn);
        assertTrue(triggered);
    }

}