package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class TargetsFilters {

    private TargetsFilters(){}

    public static final PlayersFilter noPlayersFilter = (shooter, players, squares) -> Collections.emptyList();

    public static final SquaresFilter noSquaresFilter = (shooter, players, squares) -> Collections.emptyList();

    public static List<Player> visiblePlayers(Player player, List<Player> alreadyAdded, int maxTargets) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> temp = player.gameBoard.getInRangePlayers(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> visibleSquares(Player player, List<Square> alreadyAdded, int maxTargets) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Square> temp = player.gameBoard.getInRangeSquares(player);
        temp = player.gameBoard.removeNonPlayerSquares(player, temp);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Player> awayPlayers(Player player, List<Player> alreadyAdded, int maxTargets, int minDistance) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> temp = player.gameBoard.getAwayPlayers(player, minDistance);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Player> nearPlayers(Player player, List<Player> alreadyAdded, int maxTargets, int maxDistance) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> temp = player.gameBoard.getNearPlayers(player, maxDistance);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Player> betweenPlayers(Player player, List<Player> alreadyAdded, int maxTargets, int minDistance, int maxDistance) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> temp = player.gameBoard.getBetweenPlayers(player, minDistance, maxDistance);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> nearSquares(Player player, List<Square> alreadyAdded, int maxTargets, int maxDistance) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Square> temp = player.gameBoard.getNearSquares(player, maxDistance);
        temp = player.gameBoard.removeNonPlayerSquares(player, temp);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> betweenSquares(Player player, List<Square> alreadyAdded, int maxTargets, int minDistance, int maxDistance) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Square> temp = player.gameBoard.getBetweenSquares(player, minDistance, maxDistance);
        temp = player.gameBoard.removeNonPlayerSquares(player, temp);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Player> nonVisiblePlayers(Player player, List<Player> alreadyAdded, int maxTargets) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Player> temp = player.gameBoard.getNonVisiblePlayers(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> otherVisibleRoom(Player player, List<Square> alreadyAdded, int maxTargets) {
        if(alreadyAdded.size() >= maxTargets)
            return Collections.emptyList();
        List<Square> temp = player.gameBoard.getOtherVisibleRoom(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> movable1Square(List<Player> players, List<Square> squares) {
        if(players.isEmpty() || !squares.isEmpty())
            return Collections.emptyList();
        return players.get(0).gameBoard.getSquares(players.get(0), 1);
    }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF SPECIFIC TARGET FILTERS

    public static List<Player> thorVisiblePlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 3)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return player.gameBoard.getInRangePlayers(player);
        List<Player> temp = player.gameBoard.getInRangePlayers(alreadyAdded.get(alreadyAdded.size()-1));
        temp.remove(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Player> tractorBeamPlayers1(Player player, List<Player> players) {
        if(!players.isEmpty())
            return Collections.emptyList();
        List<Square> shooterVisibleSquares = player.gameBoard.getInRangeSquares(player);
        List<Player> allPlayers = new ArrayList<>();
        for(Square square : player.gameBoard.getSquares())
            allPlayers.addAll(square.getPlayers());
        allPlayers.remove(player);
        LinkedHashSet<Player> temp = new LinkedHashSet<>();
        for(Player p : allPlayers)
            for(Square s : p.gameBoard.getSquares(p, 2))
                if(shooterVisibleSquares.contains(s))
                    temp.add(p);
        return new ArrayList<>(temp);
    }

    public static List<Square> tractorBeamSquares1(Player player, List<Player> players, List<Square> squares) {
        if(players.isEmpty() || !squares.isEmpty())
            return Collections.emptyList();
        List<Square> shooterVisibleSquares = player.gameBoard.getInRangeSquares(player);
        LinkedHashSet<Square> temp = new LinkedHashSet<>();
        for(Square s : players.get(0).gameBoard.getSquares(players.get(0), 2))
            if(shooterVisibleSquares.contains(s))
                temp.add(s);
        return new ArrayList<>(temp);
    }

    public static List<Player> tractorBeamPlayers2(Player player, List<Player> players) {
        if(!players.isEmpty())
            return Collections.emptyList();
        List<Square> tempSquares = player.gameBoard.getSquares(player, 2);
        List<Player> temp = new ArrayList<>();
        for(Square square : tempSquares)
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Square> tractorBeamSquares2(Player player, List<Player> players, List<Square> squares) {
        if(players.isEmpty() || !squares.isEmpty())
            return Collections.emptyList();
        return Collections.singletonList(player.getCurrentSquare());
    }

    public static List<Square> vortexCannonSquares(Player player, List<Square> alreadyAdded) {
        if(!alreadyAdded.isEmpty())
            return Collections.emptyList();
        List<Square> temp = new ArrayList<>();
        for(Square square : player.gameBoard.getInRangeSquares(player))
            if(!square.getPlayers().isEmpty())
                temp.add(square);
            else
                for(Square s : player.gameBoard.distanceOneSquares(square))
                    if((s.getPlayers().size() >= 2 || (!s.getPlayers().isEmpty() && s != player.getCurrentSquare())) && !temp.contains(square))
                        temp.add(square);
        temp.remove(player.getCurrentSquare());
        return temp;
    }

    public static List<Player> vortexCannonPlayers1(Player player, List<Player> players, List<Square> squares) {
        if(!players.isEmpty() || squares.isEmpty())
            return Collections.emptyList();
        List<Player> temp = new ArrayList<>();
        for(Square s : player.gameBoard.distanceOneSquares(squares.get(0)))
            temp.addAll(s.getPlayers());
        return temp;
    }

    public static List<Player> vortexCannonPlayers2(Player player, List<Player> players, List<Square> squares) {
        if(squares.isEmpty() || players.size() >= 3)
            return Collections.emptyList();
        List<Player> temp = new ArrayList<>();
        for(Square s : player.gameBoard.distanceOneSquares(squares.get(0)))
            temp.addAll(s.getPlayers());
        temp.remove(player);
        temp.removeAll(players);
        return temp;
    }

    public static List<Player> flamethrowerPlayers(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        List<Square> tempSquares;
        if(alreadyAdded.isEmpty()) {
            tempSquares = player.gameBoard.removeEmptySquares(player.gameBoard.sameDirection(player));
            tempSquares.remove(player.getCurrentSquare());
        }
        else
            tempSquares = player.gameBoard.sameDirectionSquare(player, alreadyAdded.get(0).getCurrentSquare());
        List<Player> temp = new ArrayList<>();
        for(Square square : tempSquares)
            temp.addAll(square.getPlayers());
        temp.remove(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> flamethrowerSquares(Player player, List<Square> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        if(alreadyAdded.isEmpty())
            return flamethrowerFirstSquare(player);
        List<Square> temp = player.gameBoard.sameDirectionSquare(player, alreadyAdded.get(0));
        temp.removeAll(alreadyAdded);
        return temp;
    }

    private static List<Square> flamethrowerFirstSquare(Player player) {
        List<Square> temp = new ArrayList<>();
        List<Square> tempSquares = player.gameBoard.getSquares(player, 1);
        for(Square square : tempSquares)
            if(!square.getPlayers().isEmpty())
                temp.add(square);
            else
                for(Square s : player.gameBoard.sameDirectionSquare(player, square))
                    if(!s.getPlayers().isEmpty())
                        temp.add(square);
        temp.remove(player.getCurrentSquare());
        return temp;
    }

    public static List<Player> railgunPlayers1(Player player, List<Player> alreadyAdded) {
        if(!alreadyAdded.isEmpty())
            return Collections.emptyList();
        List<Player> temp = new ArrayList<>();
        for(Square square : player.gameBoard.throughWalls(player))
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Player> railgunPlayers2(Player player, List<Player> alreadyAdded) {
        if(alreadyAdded.size() >= 2)
            return Collections.emptyList();
        List<Square> tempSquares;
        if(alreadyAdded.isEmpty())
            return railgunPlayers1(player, alreadyAdded);

        tempSquares = player.gameBoard.throughWalls(player, alreadyAdded.get(0).getCurrentSquare());
        List<Player> temp = new ArrayList<>();
        for(Square square : tempSquares)
            temp.addAll(square.getPlayers());
        temp.remove(player);
        temp.removeAll(alreadyAdded);
        return temp;
    }

    public static List<Square> sledgehammerSquares(List<Player> players, List<Square> squares) {
        if(players.isEmpty() || !squares.isEmpty())
            return Collections.emptyList();
        return players.get(0).gameBoard.sameDirection(players.get(0));
    }

    //------------------------------------------------------------------------------------------------------------------
    // POWER UP'S TARGET FILTERS

    public static List<Player> newtonPlayers(Player player, List<Player> players) {
        if(!players.isEmpty())
            return Collections.emptyList();
        List<Player> temp = new ArrayList<>();
        for(Square square : player.gameBoard.getSquares())
            temp.addAll(square.getPlayers());
        temp.remove(player);
        return temp;
    }

    public static List<Square> newtonSquares(List<Player> players, List<Square> squares) {
        if(players.isEmpty() || !squares.isEmpty())
            return Collections.emptyList();
        List<Square> temp = players.get(0).gameBoard.sameDirection(players.get(0));
        temp.remove(players.get(0).getCurrentSquare());
        return temp;
    }

    public static List<Square> teleporterSquares(Player player, List<Square> squares) {
        if(!squares.isEmpty())
            return Collections.emptyList();
        List<Square> temp = player.gameBoard.getSquares();
        temp.remove(player.getCurrentSquare());
        return temp;
    }
}
