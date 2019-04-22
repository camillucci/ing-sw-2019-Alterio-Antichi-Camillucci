package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.generics.Pair;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.Collections;
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

    public static List<Player> nonVisiblePlayers(Player player) {
        return player.getGameBoard().getNonVisiblePlayers(player);
    }

    public static List<Square> otherVisibleRoom(Player player) {
        return player.getGameBoard().getOtherVisibleRoom(player);
    }

    //------------------------------------------------------------------------------------------------------------------

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return player.getGameBoard().getInRangePlayers(player);
        List<Player> temp = player.getGameBoard().getInRangePlayers(alreadyAdded.get(alreadyAdded.size()-1));
        temp.remove(player);
        return temp;
    }

    public static List<Player> tractorBeamVisiblePlayers1(Player player) {
        List<Square> shooterVisibleSquares = player.getGameBoard().getInRangeSquares(player);
        List<Player> players = player.getGameBoard().getPlayers();
        List<Player> temp = new ArrayList<>();
        for(Player p : players) {
            List<Square> playersVisibleSquares = p.getGameBoard().getInRangeSquares(p);
            for(Square s : playersVisibleSquares) {
                if (shooterVisibleSquares.contains(s)) {
                    temp.add(p);
                    break;
                }
            }
        }
        temp.remove(player);
        return temp;
    }

    public static List<Player> tractorBeamVisiblePlayers2(Player player) {
        List<Square> shooterNearSquares = player.getGameBoard().getSquares(player, 2);
        List<Player> temp = new ArrayList<>();
        for(Square s : shooterNearSquares) {
            temp.addAll(s.getPlayers());
        }
        temp.remove(player);
        return temp;
    }

    public static List<Player> sameDirectionVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        List<Square> tempSquares;
        if(alreadyAdded.isEmpty())
            tempSquares = sameDirectionVisibleSquares(player, Collections.emptyList());
        else
            tempSquares = sameDirectionVisibleSquares(player, Collections.singletonList(alreadyAdded.get(0).getCurrentSquare()));
        List<Player> temp = new ArrayList<>();
        for(Square square : tempSquares) {
            temp.addAll(square.getPlayers());
        }
        temp.remove(player);
        return temp;
    }

    public static List<Square> sameDirectionVisibleSquares(Player player, List<Square> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return player.getGameBoard().sameDirection(player);
        return player.getGameBoard().sameDirectionSquare(player, alreadyAdded.get(0));
    }

    public static List<Pair<Player, Square>> grenadeLauncherMovablePlayers(Player player) {
        return move1Shooted(visiblePlayers(player));
    }

    public static List<Pair<Player, Square>> rocketLauncherMovablePlayers(Player player) {
        return move1Shooted(awayPlayers(player, 1));
    }

    public static List<Pair<Player, Square>> shotgunMovablePlayers(Player player) {
        return move1Shooted(nearPlayers(player, 0));
    }

    private static List<Pair<Player, Square>> move1Shooted(List<Player> targets) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : targets) {
            List<Square> tempSquares = betweenSquares(p, 1, 1);
            for(Square s : tempSquares) {
                temp.add(new Pair<>(p, s));
            }
        }
        return temp;
    }
}
