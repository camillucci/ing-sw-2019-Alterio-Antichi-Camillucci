package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.SquareBorder.*;

public class GameBoard {

    private List<Player> players = new ArrayList<>();
    public final WeaponDeck weaponDeck = new WeaponDeck();
    public final PowerUpDeck powerupDeck = new PowerUpDeck();
    public final AmmoDeck ammoDeck = new AmmoDeck();
    public final Square[][] squares = new Square[3][4];
    private int skulls;
    private int gameSize;
    private List<List<PlayerColor>> killShotTrack = new ArrayList<>();
    private static final int MAX_SKULLS = 8;

    public GameBoard(int gameLength, int gameSize) {
        this.skulls = gameLength;
        this.gameSize = gameSize;
        for(int i = 0; i < MAX_SKULLS - skulls; i++)
            killShotTrack.add(new ArrayList<>());

        if(gameSize == 10) {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, ammoDeck);
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, NOTHING}, weaponDeck);
            squares[0][3] = null;
            squares[1][0] = new SpawnAndShopSquare(1, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, weaponDeck);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{WALL, DOOR, ROOM, ROOM}, ammoDeck);
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, WALL, ROOM, DOOR}, ammoDeck);
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{NOTHING, ROOM, DOOR, NOTHING}, ammoDeck);
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck);
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{WALL, NOTHING, ROOM, DOOR}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(2, 3, new SquareBorder[]{ROOM, NOTHING, DOOR, NOTHING}, weaponDeck);
        }
        else if (gameSize == 11) {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, DOOR, NOTHING, ROOM}, ammoDeck);
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, weaponDeck);
            squares[0][3] = new AmmoSquare(0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck);
            squares[1][0] = new SpawnAndShopSquare(1, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, weaponDeck);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{WALL, DOOR, ROOM, WALL}, ammoDeck);
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck);
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck);
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, DOOR}, ammoDeck);
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(2, 3, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, weaponDeck);
        }
        else {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, DOOR}, ammoDeck);
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, DOOR, DOOR, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, weaponDeck);
            squares[0][3] = new AmmoSquare(0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck);
            squares[1][0] = new SpawnAndShopSquare(1, 0, new SquareBorder[]{ROOM, DOOR, NOTHING, WALL}, weaponDeck);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{DOOR, DOOR, WALL, WALL}, ammoDeck);
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck);
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck);
            squares[2][0] = new AmmoSquare(2, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck);
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, ROOM, DOOR}, ammoDeck);
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(2, 3, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, weaponDeck);
        }
    }

    public void addKillShotTrack(List<PlayerColor> newKillShot) {
        if(killShotTrack.size() < MAX_SKULLS)
            killShotTrack.add(newKillShot);
        else
            killShotTrack.get(MAX_SKULLS - 1).addAll(newKillShot);
    }

    public List<Square> getSquares(Player player, int dist) {

        if(dist == -1)
            return Collections.emptyList();

        if(dist == 0)
            return Collections.singletonList(player.getCurrentSquare());

        //Add all valid squares of distance 1
        List<Square> tempSquare = distanceOneSquares(player.getCurrentSquare());

        // For each number beyond 1 add all valid squares of distance 1 not already added
        int j = 1;
        for(int i = 1, temp = tempSquare.size(); i < dist; i++, temp = tempSquare.size())
            for(; j < temp; j++)
                addIfOk(tempSquare, tempSquare.get(j));
        return tempSquare;
    }

    private void addIfOk(List<Square> tempSquare, Square square) {
        if ((square.okNorth()) && !tempSquare.contains(squares[square.y - 1][square.x]))
            tempSquare.add(squares[square.y - 1][square.x]);
        if ((square.okSouth()) && !tempSquare.contains(squares[square.y + 1][square.x]))
            tempSquare.add(squares[square.y + 1][square.x]);
        if ((square.okWest()) && !tempSquare.contains(squares[square.y][square.x - 1]))
            tempSquare.add(squares[square.y][square.x - 1]);
        if ((square.okEast()) && !tempSquare.contains(squares[square.y][square.x + 1]))
            tempSquare.add(squares[square.y][square.x + 1]);
    }

    public List<Square> distanceOneSquares(Square square) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(square);

        if (tempSquare.get(0).okNorth())
            tempSquare.add(squares[tempSquare.get(0).y - 1][tempSquare.get(0).x]);
        if (tempSquare.get(0).okSouth())
            tempSquare.add(squares[tempSquare.get(0).y + 1][tempSquare.get(0).x]);
        if (tempSquare.get(0).okWest())
            tempSquare.add(squares[tempSquare.get(0).y][tempSquare.get(0).x - 1]);
        if (tempSquare.get(0).okEast())
            tempSquare.add(squares[tempSquare.get(0).y][tempSquare.get(0).x + 1]);

        return tempSquare;
    }

    public List<Square> getInRangeSquares(Player player) {
        //Add rooms near the player's current square...
        List<Square> tempSquare = distanceOneBorderType(player.getCurrentSquare(), DOOR);

        //...and add the other squares of those room
        for(int i = 0; i < tempSquare.size(); i++) {
            if(tempSquare.get(i).north == ROOM && !tempSquare.contains(squares[tempSquare.get(i).y - 1][tempSquare.get(i).x]))
                tempSquare.add(squares[tempSquare.get(i).y - 1][tempSquare.get(i).x]);
            if(tempSquare.get(i).south == ROOM && !tempSquare.contains(squares[tempSquare.get(i).y + 1][tempSquare.get(i).x]))
                tempSquare.add(squares[tempSquare.get(i).y + 1][tempSquare.get(i).x]);
            if(tempSquare.get(i).west == ROOM && !tempSquare.contains(squares[tempSquare.get(i).y][tempSquare.get(i).x - 1]))
                tempSquare.add(squares[tempSquare.get(i).y][tempSquare.get(i).x - 1]);
            if(tempSquare.get(i).east == ROOM && !tempSquare.contains(squares[tempSquare.get(i).y][tempSquare.get(i).x + 1]))
                tempSquare.add(squares[tempSquare.get(i).y][tempSquare.get(i).x + 1]);
        }
        return tempSquare;
    }

    private List<Square> distanceOneBorderType(Square square, SquareBorder type) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(square);

        if(tempSquare.get(0).north == type)
            tempSquare.add(squares[tempSquare.get(0).y - 1][tempSquare.get(0).x]);
        if(tempSquare.get(0).south == type)
            tempSquare.add(squares[tempSquare.get(0).y + 1][tempSquare.get(0).x]);
        if(tempSquare.get(0).west == type)
            tempSquare.add(squares[tempSquare.get(0).y][tempSquare.get(0).x - 1]);
        if(tempSquare.get(0).east == type)
            tempSquare.add(squares[tempSquare.get(0).y][tempSquare.get(0).x + 1]);

        return tempSquare;
    }

    public List<Player> getInRangePlayers(Player player) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare)
            tempPlayers.addAll(s.getPlayers());
        tempPlayers.remove(player);
        return tempPlayers;
    }

    public List<Player> getAwayPlayers(Player player, int minDistance){
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempCloseSquare = getSquares(player, minDistance - 1);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare) {
            if (!tempCloseSquare.contains(s))
                tempPlayers.addAll(s.getPlayers());
        }
        tempPlayers.remove(player);
        return tempPlayers;
    }

    public List<Player> getNearPlayers(Player player, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare) {
            if (tempFarSquares.contains(s))
                tempPlayers.addAll(s.getPlayers());
        }
        tempPlayers.remove(player);
        return tempPlayers;
    }

    public List<Player> getBetweenPlayers(Player player, int minDistance, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempNearSquare = getSquares(player, minDistance - 1);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare) {
            if (tempFarSquares.contains(s) && !tempNearSquare.contains(s))
                tempPlayers.addAll(s.getPlayers());
        }
        tempPlayers.remove(player);
        return tempPlayers;
    }

    public List<Square> getAwaySquares(Player player, int minDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempNearSquare = getSquares(player, minDistance - 1);
        List<Square> tempFarSquares = new ArrayList<>();
        for(Square s : tempSquare)
            if(!tempNearSquare.contains(s))
                tempFarSquares.add(s);
        return tempFarSquares;
    }

    public List<Square> getNearSquares(Player player, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Square> tempNearSquare = new ArrayList<>();
        for(Square s : tempSquare)
            if(tempFarSquares.contains(s))
                tempNearSquare.add(s);
        return tempNearSquare;
    }

    public List<Square> getBetweenSquares(Player player, int minDistance, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempNearSquare = getSquares(player, minDistance - 1);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Square> tempBetweenSquare = new ArrayList<>();
        for(Square s : tempSquare)
            if(tempFarSquares.contains(s) && !tempNearSquare.contains(s))
                tempBetweenSquare.add(s);
        return tempBetweenSquare;
    }

    public List<Square> getOtherVisibleRoom(Player player) {
        List<Square> tempSquare = new ArrayList<>();
        List<Square> tempDoors = distanceOneBorderType(player.getCurrentSquare(), DOOR);
        tempDoors.remove(player.getCurrentSquare());
        List<Square> tempRoom;
        for(Square square : tempDoors) {
            tempRoom = getRoom(square);
            for(Square s : tempRoom)
                if(!s.getPlayers().isEmpty())
                    tempSquare.add(square);
        }
        return tempSquare;
    }

    public List<Player> getNonVisiblePlayers(Player player) {
        List<Player> tempInRangePlayers = getInRangePlayers(player);
        List<Player> tempPlayers = new ArrayList<>();
        for(Player p : players) {
            if (!tempInRangePlayers.contains(p))
                tempPlayers.add(p);
        }
        tempPlayers.remove(player);
        return tempPlayers;
    }

    public List<Square> getRoom(Square square) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(square);
        for(int i = 0; i < tempSquare.size(); i++) {
            List<Square> tempNear = distanceOneBorderType(tempSquare.get(i), ROOM);
            for(Square s : tempNear)
                if(!tempSquare.contains(s))
                    tempSquare.add(s);
        }
        return tempSquare;
    }

    public List<Square> sameDirection(Player player) {
        Square tempSquare = player.getCurrentSquare();
        Square tempToAdd;
        List<Square> temp = new ArrayList<>();
        temp.add(player.getCurrentSquare());
        if (tempSquare.okNorth()) {
            tempToAdd = squares[tempSquare.y - 1][tempSquare.x];
            temp.add(tempToAdd);
            if(tempToAdd.okNorth())
                temp.add(squares[tempToAdd.y - 1][tempToAdd.x]);
        }
        if (tempSquare.okSouth()) {
            tempToAdd = squares[tempSquare.y + 1][tempSquare.x];
            temp.add(tempToAdd);
            if(tempToAdd.okSouth())
                temp.add(squares[tempToAdd.y + 1][tempToAdd.x]);
        }
        if (tempSquare.okWest()) {
            tempToAdd = squares[tempSquare.y][tempSquare.x - 1];
            temp.add(tempToAdd);
            if(tempToAdd.okWest())
                temp.add(squares[tempToAdd.y][tempToAdd.x - 1]);
        }
        if (tempSquare.okEast()) {
            tempToAdd = squares[tempSquare.y][tempSquare.x + 1];
            temp.add(tempToAdd);
            if(tempToAdd.okEast())
                temp.add(squares[tempToAdd.y][tempToAdd.x + 1]);
        }
        return temp;
    }

    public List<Square> sameDirectionSquare(Player player, Square square) {
        if(Math.abs(player.getCurrentSquare().y - square.y) == 2)
            return removeEmptySquares(Collections.singletonList(squares[(player.getCurrentSquare().y + square.y) / 2][square.x]));
        if(Math.abs(player.getCurrentSquare().x - square.x) == 2)
            return removeEmptySquares(Collections.singletonList(squares[square.y][(player.getCurrentSquare().x + square.x) / 2]));
        if(player.getCurrentSquare().y - square.y == 1 && square.okNorth())
            return removeEmptySquares(Collections.singletonList(squares[square.y - 1][square.x]));
        if(player.getCurrentSquare().y - square.y == -1 && square.okSouth())
            return removeEmptySquares(Collections.singletonList(squares[square.y + 1][square.x]));
        if(player.getCurrentSquare().x - square.x == 1 && square.okWest())
            return removeEmptySquares(Collections.singletonList(squares[square.y][square.x - 1]));
        if(player.getCurrentSquare().x - square.x == -1 && square.okEast())
            return removeEmptySquares(Collections.singletonList(squares[square.y][square.x + 1]));
        return Collections.emptyList();
    }

    public List<Square> throughWalls(Player player) {
        Square tempSquare = player.getCurrentSquare();
        Square tempToAdd;
        List<Square> temp = new ArrayList<>();

        if(player.getCurrentSquare().getPlayers().size() > 1)
            temp.add(player.getCurrentSquare());
        if (tempSquare.existNorth()) {
            tempToAdd = squares[tempSquare.y - 1][tempSquare.x];
            temp.add(tempToAdd);
            if(tempToAdd.existNorth())
                temp.add(squares[tempToAdd.y - 1][tempToAdd.x]);
        }
        if (tempSquare.existSouth()) {
            tempToAdd = squares[tempSquare.y + 1][tempSquare.x];
            temp.add(tempToAdd);
            if(tempToAdd.existSouth())
                temp.add(squares[tempToAdd.y + 1][tempToAdd.x]);
        }
        if (tempSquare.existWest()) {
            tempToAdd = squares[tempSquare.y][tempSquare.x - 1];
            temp.add(tempToAdd);
            if(tempToAdd.existWest())
                temp.add(squares[tempToAdd.y][tempToAdd.x - 1]);
        }
        if (tempSquare.existEast()) {
            tempToAdd = squares[tempSquare.y][tempSquare.x + 1];
            temp.add(tempToAdd);
            if(tempToAdd.existEast())
                temp.add(squares[tempToAdd.y][tempToAdd.x + 1]);
        }
        return removeEmptySquares(temp);
    }

    public List<Square> throughWalls(Player player, Square square) {
        List<Square> temp = new ArrayList<>();
        temp.add(player.getCurrentSquare());
        if(player.getCurrentSquare().x > square.x) {
            return throughWallsDirection(temp, 0);
        }
        if(player.getCurrentSquare().x < square.x) {
            return throughWallsDirection(temp, 1);
        }
        if(player.getCurrentSquare().y > square.y) {
            return throughWallsDirection(temp, 2);
        }
        return throughWallsDirection(temp, 3);
    }

    private List<Square> throughWallsDirection(List<Square> temp, int val) {
        for(int i = 0; i < temp.size(); i++)
            switch(val) {
                case 0:
                    if(temp.get(i).existWest())
                        temp.add(squares[temp.get(i).y][temp.get(i).x - 1]);
                    break;
                case 1:
                    if(temp.get(i).existEast())
                        temp.add(squares[temp.get(i).y][temp.get(i).x + 1]);
                    break;
                case 2:
                    if(temp.get(i).existNorth())
                        temp.add(squares[temp.get(i).y - 1][temp.get(i).x]);
                    break;
                default:
                    if(temp.get(i).existSouth())
                        temp.add(squares[temp.get(i).y + 1][temp.get(i).x]);
            }
        return temp;
    }

    public List<Square> removeEmptySquares(List<Square> squareList) {
        List<Square> temp = new ArrayList<>();
        for (Square square : squareList)
            if (!square.getPlayers().isEmpty())
                temp.add(square);
        return temp;
    }

    public List<Square> removeNonPlayerSquares(Player player, List<Square> squareList) {
        List<Square> temp = removeEmptySquares(squareList);
        if(player.getCurrentSquare().getPlayers().size() == 1)
            temp.remove(player.getCurrentSquare());
        return temp;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public int getSkulls() {
        return skulls;
    }

    public List<List<PlayerColor>> getKillShotTrack() {
        return killShotTrack;
    }

    public Square getSpawnAndShopSquare(AmmoColor color) {
        switch (color) {
            case BLUE:
                return squares[0][2];
            case RED:
                return squares[1][0];
            default:
                return squares[2][3];
        }
    }

    public List<Square> getSquares() {
        List<Square> temp = new ArrayList<>();
        for (Square[] square : squares)
            for(Square s : square)
                if(s != null)
                    temp.add(s);
        return temp;
    }

    public int getGameSize() {
        return gameSize;
    }
}
