package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;

import java.util.List;

public class TargetsFilters
{
    public static List<Player> visiblePlayers(Player player)
    {
        return player.getGameBoard().getInRangePlayers(player);
    }
}
