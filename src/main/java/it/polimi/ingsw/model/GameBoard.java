package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.SquareBorder.*;

/**
 * This class is the board of the game, it contains all the squares, the kill shot track
 * and all the method to calculate the required squares for actions
 */
public class GameBoard {

    /**
     * The list of player currently playing the match
     */
    private List<Player> players = new ArrayList<>();
    /**
     * The deck with all remaining WeaponCards
     */
    public final WeaponDeck weaponDeck = new WeaponDeck();
    /**
     * The deck with all remaining and discarded PowerUpCards
     */
    public final PowerUpDeck powerupDeck = new PowerUpDeck();
    /**
     * The deck with all remaining and discarded AmmoCards
     */
    public final AmmoDeck ammoDeck = new AmmoDeck();
    /**
     * The matrix of squares
     */
    public final Square[][] squares = new Square[3][4];
    /**
     * The number of skulls remaining, from 0 to 8
     */
    private int skulls;
    /**
     * The type of the map, from 0 to 3
     */
    private int mapType;
    /**
     * The kill shot track, it contains the piece that identify how many kills each person got
     */
    private List<List<PlayerColor>> killShotTrack = new ArrayList<>();
    private static final int MAX_SKULLS = 8;
    private static final String BLUE_SPAWN_NAME = "Blue Spawn";
    private static final String RED_SPAWN_NAME = "Red Spawn";
    private static final String YELLOW_SPAWN_NAME = "Yellow Spawn";

    /**
     * Create all the squares and the kill shot track
     * @param gameLength The number of skulls, which define the length of the match, from 5 to 8
     * @param mapType The type of the map, from 0 to 3
     */
    public GameBoard(int gameLength, int mapType) {
        this.skulls = gameLength;
        this.mapType = mapType;
        for(int i = 0; i < MAX_SKULLS - skulls; i++)
            killShotTrack.add(new ArrayList<>());

        if(mapType == 0) {
            squares[0][0] = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, ammoDeck);
            squares[0][1] = new AmmoSquare("B", 0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(BLUE_SPAWN_NAME, 0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, NOTHING}, weaponDeck);
            squares[0][3] = null;
            squares[1][0] = new SpawnAndShopSquare(RED_SPAWN_NAME, 1, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, weaponDeck);
            squares[1][1] = new AmmoSquare("C", 1, 1, new SquareBorder[]{WALL, DOOR, ROOM, ROOM}, ammoDeck);
            squares[1][2] = new AmmoSquare("D", 1, 2, new SquareBorder[]{DOOR, WALL, ROOM, DOOR}, ammoDeck);
            squares[1][3] = new AmmoSquare("E", 1, 3, new SquareBorder[]{NOTHING, ROOM, DOOR, NOTHING}, ammoDeck);
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare("F", 2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck);
            squares[2][2] = new AmmoSquare("G", 2, 2, new SquareBorder[]{WALL, NOTHING, ROOM, DOOR}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(YELLOW_SPAWN_NAME, 2, 3, new SquareBorder[]{ROOM, NOTHING, DOOR, NOTHING}, weaponDeck);
        }
        else if (mapType == 1) {
            squares[0][0] = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, DOOR, NOTHING, ROOM}, ammoDeck);
            squares[0][1] = new AmmoSquare("B", 0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(BLUE_SPAWN_NAME, 0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, weaponDeck);
            squares[0][3] = new AmmoSquare("C", 0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck);
            squares[1][0] = new SpawnAndShopSquare(RED_SPAWN_NAME, 1, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, weaponDeck);
            squares[1][1] = new AmmoSquare("D", 1, 1, new SquareBorder[]{WALL, DOOR, ROOM, WALL}, ammoDeck);
            squares[1][2] = new AmmoSquare("E", 1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck);
            squares[1][3] = new AmmoSquare("F", 1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck);
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare("G", 2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, DOOR}, ammoDeck);
            squares[2][2] = new AmmoSquare("H", 2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(YELLOW_SPAWN_NAME, 2, 3, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, weaponDeck);
        }
        else if (mapType == 2) {
            squares[0][0] = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, DOOR}, ammoDeck);
            squares[0][1] = new AmmoSquare("B", 0, 1, new SquareBorder[]{NOTHING, DOOR, DOOR, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(BLUE_SPAWN_NAME, 0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, NOTHING}, weaponDeck);
            squares[0][3] = null;
            squares[1][0] = new SpawnAndShopSquare(RED_SPAWN_NAME, 1, 0, new SquareBorder[]{ROOM, DOOR, NOTHING, WALL}, weaponDeck);
            squares[1][1] = new AmmoSquare("C", 1, 1, new SquareBorder[]{DOOR, DOOR, WALL, ROOM}, ammoDeck);
            squares[1][2] = new AmmoSquare("D", 1, 2, new SquareBorder[]{DOOR, WALL, ROOM, DOOR}, ammoDeck);
            squares[1][3] = new AmmoSquare("E", 1, 3, new SquareBorder[]{NOTHING, ROOM, DOOR, NOTHING}, ammoDeck);
            squares[2][0] = new AmmoSquare("F", 2, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck);
            squares[2][1] = new AmmoSquare("G", 2, 1, new SquareBorder[]{DOOR, NOTHING, ROOM, ROOM}, ammoDeck);
            squares[2][2] = new AmmoSquare("H", 2, 2, new SquareBorder[]{WALL, NOTHING, ROOM, DOOR}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(YELLOW_SPAWN_NAME, 2, 3, new SquareBorder[]{ROOM, NOTHING, DOOR, NOTHING}, weaponDeck);
        }
        else {
            squares[0][0] = new AmmoSquare("A", 0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, DOOR}, ammoDeck);
            squares[0][1] = new AmmoSquare("B", 0, 1, new SquareBorder[]{NOTHING, DOOR, DOOR, ROOM}, ammoDeck);
            squares[0][2] = new SpawnAndShopSquare(BLUE_SPAWN_NAME, 0, 2, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, weaponDeck);
            squares[0][3] = new AmmoSquare("C", 0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck);
            squares[1][0] = new SpawnAndShopSquare(RED_SPAWN_NAME, 1, 0, new SquareBorder[]{ROOM, DOOR, NOTHING, WALL}, weaponDeck);
            squares[1][1] = new AmmoSquare("D", 1, 1, new SquareBorder[]{DOOR, DOOR, WALL, WALL}, ammoDeck);
            squares[1][2] = new AmmoSquare("E", 1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck);
            squares[1][3] = new AmmoSquare("F", 1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck);
            squares[2][0] = new AmmoSquare("G", 2, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck);
            squares[2][1] = new AmmoSquare("H", 2, 1, new SquareBorder[]{DOOR, NOTHING, ROOM, DOOR}, ammoDeck);
            squares[2][2] = new AmmoSquare("I", 2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck);
            squares[2][3] = new SpawnAndShopSquare(YELLOW_SPAWN_NAME, 2, 3, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, weaponDeck);
        }
    }

    /**
     * Add a new list to the kill shot track each time a player is killed
     * @param newKillShot The new kill shot track to add, one PlayerColor for the death and a second one for the overkill
     */
    public void addKillShotTrack(List<PlayerColor> newKillShot) {
        if(killShotTrack.size() < MAX_SKULLS)
            killShotTrack.add(newKillShot);
        else
            killShotTrack.get(MAX_SKULLS - 1).addAll(newKillShot);
    }

    /**
     * This method returns all the squares a Player can move into
     * @param player The Player from which calculate di squares
     * @param dist The maximum distance a player can move
     * @return The list of squares which are at maximum at distance dist from the Player
     */
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

    /**
     * This method adds to a list all the squares near a given square if they are reachable and if they are not already contained
     * @param tempSquare The list to which the new squares are added
     * @param square The square from which the near squares are gonna be evaluated
     */
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

    /**
     * This method returns all the near square from a given square
     * @param square The Square from near squares are found
     * @return The list of near squares from the given Square
     */
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

    /**
     * This method returns all square a given player can see
     * @param player The Player from which we need to fine the squares it can see
     * @return The squares a given Player can see
     */
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

    /**
     * This method returns all near squares from a given square if the are separated by a given SquareBorder
     * @param square The Square from which its near squares are evaluated
     * @param type The SquareBorder that we need to check
     * @return All squares near the given Square which confine through the given SquareBorder
     */
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

    /**
     * This method returns all players a given player can see
     * @param player The Player from which we need to fine the players it can see
     * @return The players that the given Player can see
     */
    public List<Player> getInRangePlayers(Player player) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare)
            tempPlayers.addAll(s.getPlayers());
        tempPlayers.remove(player);
        return tempPlayers;
    }

    /**
     * This method returns all players a given player can see which are at least at a given distance
     * @param player The Player from which we need to fine the players it can see
     * @param minDistance The minimum distance the given Player can see
     * @return The players that the given Player can see which are at least at the given distance
     */
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

    /**
     * This method returns all players a given player can see which are at most at a given distance
     * @param player The Player from which we need to fine the players it can see
     * @param maxDistance The maximum distance the given Player can see
     * @return The players that the given Player can see which are at most at the given distance
     */
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

    /**
     * This method returns all players a given player can see which are between the two given distance
     * @param player The Player from which we need to fine the players it can see
     * @param minDistance The minimum distance the given Player can see
     * @param maxDistance The maximum distance the given Player can see
     * @return The players that the given Player can see which are between the two given distance
     */
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

    /**
     * This method returns all squares a given player can see which are at most at a given distance
     * @param player The Player from which we need to fine the squares it can see
     * @param maxDistance The maximum distance the given Player can see
     * @return The squares that the given Player can see which are at most at the given distance
     */
    public List<Square> getNearSquares(Player player, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Square> tempNearSquare = new ArrayList<>();
        for(Square s : tempSquare)
            if(tempFarSquares.contains(s))
                tempNearSquare.add(s);
        return tempNearSquare;
    }

    /**
     * This method returns all squares a given player can see which are between the two given distance
     * @param player The Player from which we need to fine the squares it can see
     * @param minDistance The minimum distance the given Player can see
     * @param maxDistance The maximum distance the given Player can see
     * @return The squares that the given Player can see which are between the two given distance
     */
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

    /**
     * This method returns the first square of each room a given player can see and shoot in
     * @param player The Player from which we need to fine the rooms it can see
     * @return The squares of the room that the given Player can see and shoot in
     */
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

    /**
     * This method returns all players a given Player cannot see
     * @param player The Player from which we need to fine the players it cannot see
     * @return The players that the given Player cannot see
     */
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

    /**
     * This method returns all the squares in the same room of a given Square
     * @param square The Square from which we need to find the room it is in
     * @return The squares in same room of the given Square
     */
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

    /**
     * This method return all squares in the same cardinal direction from a given Player, stopping at walls
     * @param player The Player from which we need to find all the squares in the same cardinal direction
     * @return The square in the same cardinal direction from the given Player, stopping at walls
     */
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

    /**
     * This method returns all the squares in the same cardinal direction from the Square of a given Player is in and an other given Square, stopping at walls
     * @param player The Player from which we need to find all the squares in the same direction
     * @param square The Square from which we need to fine all squares in the same direction
     * @return The squares in the same cardinal direction from the Square of the given Player is in and the given Square, stopping at walls
     */
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

    /**
     * This method return all squares in the same cardinal direction from a given Player
     * @param player The Player from which we need to find all the squares in the same cardinal direction
     * @return The square in the same cardinal direction from the given Player
     */
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

    /**
     * This method returns all the squares in the same cardinal direction from the Square of a given Player is in and an other given Square
     * @param player The Player from which we need to find all the squares in the same direction
     * @param square The Square from which we need to fine all squares in the same direction
     * @return The squares in the same cardinal direction from the Square of the given Player is in and the given Square
     */
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

    /**
     * This method is an helper of the throughWalls(Player, Square) method
     */
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

    /**
     * This method returns the list of square with at least one player on it from a given list of squares
     * @param squareList The list of squares from which we need to remove the square without player on it
     * @return The list of squares which have at least one player on it
     */
    public List<Square> removeEmptySquares(List<Square> squareList) {
        List<Square> temp = new ArrayList<>();
        for (Square square : squareList)
            if (!square.getPlayers().isEmpty())
                temp.add(square);
        return temp;
    }

    /**
     * This method returns the given list of squares without the square a given Player is on if it is the only Player on it
     * @param player The Player to check the square it is on
     * @param squareList The list of squares from which we may need to remove the Square the given Player is on
     * @return The list of squares without the Square the given Player is on if it is the only Player on it
     */
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

    /**
     * This method returns the Square of the shop of a given AmmoColor
     * @param color The AmmoColor from which we need to find the shop of the same AmmoColor
     * @return The shop of the given AmmoColor
     */
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

    /**
     * @return The squares in the form of list
     */
    public List<Square> getSquares() {
        List<Square> temp = new ArrayList<>();
        for (Square[] square : squares)
            for(Square s : square)
                if(s != null)
                    temp.add(s);
        return temp;
    }

    public int getMapType() {
        return mapType;
    }
}
