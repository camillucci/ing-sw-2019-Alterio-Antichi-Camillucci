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

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded)
    {
        Player tmp = alreadyAdded.isEmpty() ? player : alreadyAdded.get(alreadyAdded.size()-1);
        return tmp.getGameBoard().getInRangePlayers(tmp);
    }
}
