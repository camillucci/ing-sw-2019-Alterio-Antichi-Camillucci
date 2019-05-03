package it.polimi.ingsw.model;

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
    boolean triggered = false;
    @Test
    void testBranchMap()
    {
        Turn turn = new Turn(new Player("a", BLUE, new GameBoard(10,7)), match);
        turn.endTurnEvent.addEventHandler((a,b) -> endTurn = true);
        assertFalse(endTurn);

        Bot bot = new Bot();
        bot.playNoAdrenaline(turn); // shooter damaged by 3 or 4

        assertFalse(endTurn);
        bot = new Bot();
        bot.playThreeDamage(turn);
        assertTrue(endTurn);
    }

}