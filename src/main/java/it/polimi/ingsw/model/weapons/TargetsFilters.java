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

    public static List<Player> noFilters(Player player, List<Player> addedPlayers, int maxTargets)
    {
        if(addedPlayers.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> ret = player.getGameBoard().getPlayers();
        addedPlayers.forEach(ret::remove);
        return ret;
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

    public static List<Pair<Player, Square>> tractorBeamVisiblePlayers1(Player player) {
        List<Square> shooterVisibleSquares = player.getGameBoard().getInRangeSquares(player);
        List<Player> players = player.getGameBoard().getPlayers();
        players.remove(player);
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : players) {
                List<Square> playersVisibleSquares = p.getGameBoard().getInRangeSquares(p);
                for(Square s : playersVisibleSquares)
                    if(shooterVisibleSquares.contains(s))
                        temp.add(new Pair<>(p, s));
            }
        return temp;
    }

    public static List<Pair<Player, Square>> tractorBeamVisiblePlayers2(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Square s : player.getGameBoard().getSquares(player, 2))
            for(Player p : s.getPlayers())
                if(p != player)
                    temp.add(new Pair<>(p, p.getCurrentSquare()));
        return temp;
    }

    public static List<Pair<Player, Square>> vortexCannonVisiblePlayers1(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        List<Player> tempPlayers = new ArrayList<>();
        for(Square square : player.getGameBoard().getAwaySquares(player, 1)) {
            for(Square s : player.getGameBoard().distanceOneSquares(square))
                tempPlayers.addAll(s.getPlayers());
            for(Player p : tempPlayers)
                temp.add(new Pair<>(p, square));
            tempPlayers.clear();
        }
        return temp;
    }

    public static List<Pair<Player, Square>> vortexCannonVisiblePlayers2(Player player, List<Pair<Player, Square>> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return vortexCannonVisiblePlayers1(player);
        Square square = alreadyAdded.get(0).getRight();
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : player.getGameBoard().distanceOneSquares(square))
            tempPlayers.addAll(s.getPlayers());
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : tempPlayers)
            temp.add(new Pair<>(p, square));
        return temp;
    }

    public static List<Player> flamethrowerVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        List<Square> tempSquares;
        if(alreadyAdded.isEmpty())
            tempSquares = player.getGameBoard().sameDirection(player);
        else
            tempSquares = player.getGameBoard().sameDirectionSquare(player, alreadyAdded.get(0).getCurrentSquare());
        List<Player> temp = new ArrayList<>();
        for(Square square : tempSquares)
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Square> flamethrowerVisibleSquares(Player player, List<Square> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return player.getGameBoard().sameDirection(player);
        return player.getGameBoard().sameDirectionSquare(player, alreadyAdded.get(0));
    }

    public static List<Pair<Player, Square>> grenadeLauncherMovablePlayers(Player player) {
        return moveShooted(visiblePlayers(player), 1);
    }

    public static List<Pair<Player, Square>> rocketLauncherMovablePlayers(Player player) {
        return moveShooted(awayPlayers(player, 1), 1);
    }

    public static List<Player> railgunVisiblePlayer(Player player) {
        List<Player> temp = new ArrayList<>();
        for(Square square : player.getGameBoard().throughWalls(player))
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Pair<Player, Square>> shotgunMovablePlayers(Player player) {
        return moveShooted(nearPlayers(player, 0), 1);
    }

    public static List<Pair<Player, Square>> sledgehammerMovablePlayers(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : nearPlayers(player, 0)) {
            List<Square> tempSquares = player.getGameBoard().sameDirection(p);
            for(Square s : tempSquares)
                temp.add(new Pair<>(p, s));
        }
        return temp;
    }

    private static List<Pair<Player, Square>> moveShooted(List<Player> targets, int maxDistance) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : targets) {
            List<Square> tempSquares = betweenSquares(p, 0, maxDistance);
            for(Square s : tempSquares)
                temp.add(new Pair<>(p, s));
        }
        return temp;
    }
}
