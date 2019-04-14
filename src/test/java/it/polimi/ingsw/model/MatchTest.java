package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private static final int n = 4;

    @Test
    void assignPoints1() {
        List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
        List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
        Match match = new Match(names, colors, 8, 10);

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
        List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
        List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
        Match match = new Match(names, colors, 8, 12);

        Player deadPlayer = match.getPlayers().get(2);
        deadPlayer.setFinalFrenzy(true);
        for(int i = 0; i < n; i++) {
            deadPlayer.addDamage(match.getPlayers().get(0), 1);
            deadPlayer.addDamage(match.getPlayers().get(1), 2);
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints();
        assertEquals(1, match.getPlayers().get(0).getPoints());
        assertEquals(2, match.getPlayers().get(1).getPoints());
    }
}
