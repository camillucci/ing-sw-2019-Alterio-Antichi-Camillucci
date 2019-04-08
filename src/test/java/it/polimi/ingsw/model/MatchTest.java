package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.model.PlayerColor.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C"));
    List<PlayerColor> colors = new ArrayList<>(Arrays.asList(BLUE, GREEN, GREY));
    Match match = new Match(names, colors, 8, 12);

    @Test
    void assignPoints() {
        Player deadPlayer = match.getPlayers().get(2);
        for(int i = 0; i < 4; i++) {
            deadPlayer.addDamages(BLUE, 1);
            deadPlayer.addDamages(GREEN, 2);
        }
        match.addDeadPlayers(deadPlayer);
        match.assignPoints();
        assertEquals(7, match.getPlayers().get(0).getPoints());
        assertEquals(8, match.getPlayers().get(1).getPoints());
        assertEquals(2, match.getGameBoard().getKillShotTrack().get(0).size());
        assertEquals(1, match.getGameBoard().getKillShotTrack().size());
    }
}