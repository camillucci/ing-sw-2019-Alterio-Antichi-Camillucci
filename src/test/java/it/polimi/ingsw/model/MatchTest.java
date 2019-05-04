package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private static final int n = 4;
    List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
    List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
    Match match = new Match(names, colors, 5, 10);
    Bot bot = new Bot();

    public MatchTest()
    {
        initialize();
    }

    void initialize()
    {
        // spawning players
        for(int i = 0; i < match.getPlayers().size(); i++)
            bot.playSpawnBranchMap(match);
    }

    @Test
    void assignPoints1() {
        Player deadPlayer = match.getPlayers().get(2);
        for(int i = 0; i < n; i++) {
            deadPlayer.addDamage(match.getPlayers().get(0), 1);
            deadPlayer.addDamage(match.getPlayers().get(1), 2);
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints();
        assertEquals(7, match.getPlayers().get(0).getPoints());
        assertEquals(8, match.getPlayers().get(1).getPoints());
    }

    @Test
    void assignPoints2() {
        Player deadPlayer = match.getPlayers().get(2);
        deadPlayer.setFinalFrenzy(true);
        for(int i = 0; i < n; i++) {
            deadPlayer.addDamage(match.getPlayers().get(0), 1);
            deadPlayer.addDamage(match.getPlayers().get(1), 2);
            match.gameBoard.addKillShotTrack(Collections.emptyList());
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints();
        assertEquals(1, match.getPlayers().get(0).getPoints());
        assertEquals(2, match.getPlayers().get(1).getPoints());
        assertEquals(1, match.getFrenzyStarter());
    }

    @Test
    void SpawnAndFirstTurn()
    {
        for(Player p: match.getPlayers())
            assertEquals(1, p.getPowerUps().size());
        Player p = match.getPlayer();
        bot.playNoAdrenaline(match); // shooter damage damaged of 3 or 4
        bot.playThreeDamage(match);
        bot.reloadEndTurn(match);
        assertNotSame(match.getPlayer(), p);
    }
}
