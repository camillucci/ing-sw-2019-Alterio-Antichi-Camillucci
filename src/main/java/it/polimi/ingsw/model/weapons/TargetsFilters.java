package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.List;

public class TargetsFilters
{
    private TargetsFilters(){}

    public static List<Player> visiblePlayers(Player player)
    {
        return player.getGameBoard().getInRangePlayers(player);
    }

    public static List<Square> visibleSquares(Player player) {
        return player.getGameBoard().getInRangeSquares(player);
    }

    public static List<Player> awayPlayers(Player player, int minDistance) {
        return player.getGameBoard().getAwayPlayers(player, minDistance);
    }

    public static List<Player> nearPlayers(Player player, int maxDistance) {
        return player.getGameBoard().getNearPlayers(player, maxDistance);
    }

    public static List<Player> betweenPlayers(Player player, int minDistance, int maxDistance) {
        return player.getGameBoard().getBetweenPlayers(player, minDistance, maxDistance);
    }

    public static List<Square> awaySquares(Player player, int minDistance) {
        return player.getGameBoard().getAwaySquares(player, minDistance);
    }

    public static List<Square> nearSquares(Player player, int maxDistance) {
        return player.getGameBoard().getNearSquares(player, maxDistance);
    }

    public static List<Square> betweenSquares(Player player, int minDistance, int maxDistance) {
        return player.getGameBoard().getBetweenSquares(player, minDistance, maxDistance);
    }

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded)
    {
        Player tmp = alreadyAdded.isEmpty() ? player : alreadyAdded.get(alreadyAdded.size()-1);
        return tmp.getGameBoard().getInRangePlayers(tmp);
    }

    public static List<Player> tractorBeamVisiblePlayers(Player player, List<Player> players) {
        //TODO
        return null;
    }

    public static List<Player> nonVisiblePlayers(Player player) {
        return player.getGameBoard().getNonVisiblePlayers(player);
    }

    public static List<Square> otherVisibleRoom(Player player) {
        return player.getGameBoard().getOtherVisibleRoom(player);
    }
}
