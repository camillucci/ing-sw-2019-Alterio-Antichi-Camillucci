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

    public static List<Player> visiblePlayers(Player player) {
        return player.gameBoard.getInRangePlayers(player);
    }

    /*
    public static List<Player> noFilters(Player player, List<Player> addedPlayers, int maxTargets) {
        if(addedPlayers.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> ret = player.gameBoard.getPlayers();
        addedPlayers.forEach(ret::remove);
        return ret;
    }
    */

    public static List<Square> visibleSquares(Player player) {
        return player.gameBoard.getInRangeSquares(player);
    }

    public static List<Player> awayPlayers(Player player, int minDistance) {
        return player.gameBoard.getAwayPlayers(player, minDistance);
    }

    public static List<Player> nearPlayers(Player player, int maxDistance) {
        return player.gameBoard.getNearPlayers(player, maxDistance);
    }

    public static List<Player> betweenPlayers(Player player, int minDistance, int maxDistance) {
        return player.gameBoard.getBetweenPlayers(player, minDistance, maxDistance);
    }

    /*
    public static List<Square> awaySquares(Player player, int minDistance) {
        return player.gameBoard.getAwaySquares(player, minDistance);
    }
    */

    public static List<Square> nearSquares(Player player, int maxDistance) {
        return player.gameBoard.getNearSquares(player, maxDistance);
    }

    public static List<Square> betweenSquares(Player player, int minDistance, int maxDistance) {
        return player.gameBoard.getBetweenSquares(player, minDistance, maxDistance);
    }

    public static List<Player> nonVisiblePlayers(Player player) {
        return player.gameBoard.getNonVisiblePlayers(player);
    }

    public static List<Square> otherVisibleRoom(Player player) {
        return player.gameBoard.getOtherVisibleRoom(player);
    }

    //------------------------------------------------------------------------------------------------------------------

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return player.gameBoard.getInRangePlayers(player);
        List<Player> temp = player.gameBoard.getInRangePlayers(alreadyAdded.get(alreadyAdded.size()-1));
        temp.remove(player);
        return temp;
    }

    public static List<Pair<Player, Square>> tractorBeamMovableTargets1(Player player) {
        List<Square> shooterVisibleSquares = player.gameBoard.getInRangeSquares(player);
        List<Player> players = player.gameBoard.getPlayers();
        players.remove(player);
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : players) {
                List<Square> playersVisibleSquares = p.gameBoard.getNearSquares(p, 2);
                for(Square s : playersVisibleSquares)
                    if(shooterVisibleSquares.contains(s))
                        temp.add(new Pair<>(p, s));
        }
        return temp;
    }

    public static List<Pair<Player, Square>> tractorBeamMovableTargets2(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Square s : player.gameBoard.getSquares(player, 2))
            for(Player p : s.getPlayers())
                if(p != player)
                    temp.add(new Pair<>(p, p.getCurrentSquare()));
        return temp;
    }

    public static List<Pair<Player, Square>> vortexCannonMovableTargets1(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        List<Player> tempPlayers = new ArrayList<>();
        for(Square square : player.gameBoard.getAwaySquares(player, 1)) {
            for(Square s : player.gameBoard.distanceOneSquares(square))
                tempPlayers.addAll(s.getPlayers());
            for(Player p : tempPlayers)
                temp.add(new Pair<>(p, square));
            tempPlayers.clear();
        }
        return temp;
    }

    public static List<Player> vortexCannonMovablePlayers2(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return getLeftList(vortexCannonMovableTargets1(player));
        return getVortexCannonPlayer(player, alreadyAdded.get(0).getCurrentSquare());
    }

    public static List<Square> vortexCannonMovableSquares2(Player player, List<Square> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return getRightList(vortexCannonMovableTargets1(player));
        Square square = alreadyAdded.get(0);
        List<Player> tempPlayers = getVortexCannonPlayer(player, square);
        List<Square> temp = new ArrayList<>();
        for(int i = 0; i < tempPlayers.size(); i++)
            temp.add(square);
        return temp;
    }

    private static List<Player> getVortexCannonPlayer(Player player, Square square) {
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : player.gameBoard.distanceOneSquares(square))
            tempPlayers.addAll(s.getPlayers());
        return tempPlayers;
    }

    public static List<Player> flamethrowerVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        List<Square> tempSquares;
        if(alreadyAdded.isEmpty())
            tempSquares = player.gameBoard.sameDirection(player);
        else
            tempSquares = player.gameBoard.sameDirectionSquare(player, alreadyAdded.get(0).getCurrentSquare());
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
            return player.gameBoard.sameDirection(player);
        return player.gameBoard.sameDirectionSquare(player, alreadyAdded.get(0));
    }

    public static List<Pair<Player, Square>> grenadeLauncherMovableTargets(Player player) {
        return movable1Shooted(visiblePlayers(player));
    }

    public static List<Pair<Player, Square>> rocketLauncherMovableTargets(Player player) {
        return movable1Shooted(awayPlayers(player, 1));
    }

    public static List<Player> railgunVisiblePlayer(Player player) {
        List<Player> temp = new ArrayList<>();
        for(Square square : player.gameBoard.throughWalls(player))
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Pair<Player, Square>> shotgunMovableTargets(Player player) {
        return movable1Shooted(nearPlayers(player, 0));
    }

    public static List<Pair<Player, Square>> sledgehammerMovableTargets(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : nearPlayers(player, 0)) {
            List<Square> tempSquares = player.gameBoard.sameDirection(p);
            for(Square s : tempSquares)
                temp.add(new Pair<>(p, s));
        }
        return temp;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static List<Pair<Player, Square>> newtonMovableTargets(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : player.gameBoard.getPlayers())
            for(Square s : betweenSquares(p, 0, 2))
                temp.add(new Pair<>(p, s));
        return temp;
    }

    public static List<Pair<Player, Square>> teleporterMovableTargets(Player player) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Square s : player.gameBoard.getSquares())
            temp.add(new Pair<>(player, s));
        return temp;
    }
    //------------------------------------------------------------------------------------------------------------------

    private static List<Pair<Player, Square>> movable1Shooted(List<Player> targets) {
        List<Pair<Player, Square>> temp = new ArrayList<>();
        for(Player p : targets) {
            List<Square> tempSquares = nearSquares(p, 1);
            for(Square s : tempSquares)
                temp.add(new Pair<>(p, s));
        }
        return temp;
    }

    private static List<Player> getLeftList(List<Pair<Player, Square>> pairList) {
        List<Player> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getLeft());
        return temp;
    }

    private static List<Square> getRightList(List<Pair<Player, Square>> pairList) {
        List<Square> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getRight());
        return temp;
    }
}
