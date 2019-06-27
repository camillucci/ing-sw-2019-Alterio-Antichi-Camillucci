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
    private List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
    private List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
    private Match match = new Match(names, colors, 8, 0);
    private Bot bot = new Bot();
    private boolean triggered = false;

    @Test
    void test1()
    {
        Turn turn = new Turn(new Player("a", BLUE, new GameBoard(10,7)), match);
        turn.endTurnEvent.addEventHandler((a,b) -> endTurn = true);
        assertFalse(endTurn);

        bot.playNoAdrenaline(turn); // shooter damaged by 3 or 4
        assertFalse(endTurn);
        bot.playThreeDamage(turn);
        bot.reloadEndTurn(turn);
        assertTrue(endTurn);
    }

    @Test
    void test2() // rollback in Turn
    {
        Turn turn = new Turn(new Player("a", BLUE, match.getGameBoard()), match);
        turn.getPlayer().setCurrentSquare(match.getGameBoard().getSpawnAndShopSquare(AmmoColor.RED));
        turn.getPlayer().getCurrentSquare().addPlayer(turn.getPlayer());
        turn.endTurnEvent.addEventHandler((a,b) -> triggered = true);

        bot.playNoAdrenaline(turn); // shooter damaged by 3 or 4

        for(int i = 0, N = 200; i < N; i++)
        {
            turn.getActions().stream().filter(a->BranchTestUtilities.isEqual(a, new MoveAction(2))).forEach(
                    a->{a.initialize(turn.getPlayer()); a.addTarget(turn.getPlayer().getCurrentSquare()); a.doAction();});
            //BranchTestUtilities.searchAndDo(turn.getActions(), new RollBackAction());
            assertFalse(triggered);
        }

        //bot.playThreeDamage(turn);
        //bot.reloadEndTurn(turn);
        //assertTrue(triggered);
    }
}
