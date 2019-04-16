package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TargetsFilters
{
    private TargetsFilters(){}

    public static List<Player> visiblePlayers(Player player)
    {
        return player.gameBoard.getInRangePlayers(player);
    }

    public static List<Square> visibleSquares(Player player) {
        return player.gameBoard.getInRangeSquares(player);
    }

    public static List<Player> away(Player player, int distance) {
        return player.gameBoard.getAwayPlayers(player, distance);
    }

    public static List<Square> sameSquarePlayers(Player player) {
        return new ArrayList<>(Collections.singletonList((player.getCurrentSquare())));
    }

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded)
    {
        Player tmp = alreadyAdded.isEmpty() ? player : alreadyAdded.get(alreadyAdded.size()-1);
        return tmp.gameBoard.getInRangePlayers(tmp);
    }

    public static List<Player> tractorBeamVisiblePlayers(Player player, List<Player> players) {
        //TODO
        return null;
    }
}
