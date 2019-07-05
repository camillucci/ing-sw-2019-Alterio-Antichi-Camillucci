package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private static final int n = 4;
    private Match match;
    private Bot bot;

    @BeforeEach
    void setUp() {
        List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
        List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
        match = new Match(names, colors, 5, 0);
        match.start();
        bot = new Bot();

        // spawning players
        for(int i = 0; i < match.getPlayers().size(); i++) {
            bot.playSpawnBranchMap(match);
            bot.playEmptyTurn(match);
        }
    }

    @Test
    void assignPoints1() {
        Player deadPlayer = match.getPlayers().get(2);
        for(int i = 0; i < n; i++) {
            deadPlayer.addDamage(match.getPlayers().get(0), 1);
            deadPlayer.addDamage(match.getPlayers().get(1), 2);
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints(deadPlayer);
        assertEquals(7, match.getPlayers().get(0).getPoints());
        assertEquals(8, match.getPlayers().get(1).getPoints());
    }

    @Test
    void assignPoints2() {
        Player deadPlayer = match.getPlayers().get(2);
        deadPlayer.setFinalFrenzy();
        for(int i = 0; i < n; i++) {
            deadPlayer.addDamage(match.getPlayers().get(0), 1);
            deadPlayer.addDamage(match.getPlayers().get(1), 2);
            match.getGameBoard().addKillShotTrack(Collections.emptyList());
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints(deadPlayer);
        assertEquals(1, match.getPlayers().get(0).getPoints());
        assertEquals(2, match.getPlayers().get(1).getPoints());
        assertEquals(1, match.getFrenzyStarter());
    }

}
