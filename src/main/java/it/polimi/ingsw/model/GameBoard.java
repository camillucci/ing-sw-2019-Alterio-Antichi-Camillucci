package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.AmmoColor.*;
import static it.polimi.ingsw.model.SquareBorder.*;

public class GameBoard {

    private List<Player> players = new ArrayList<>();
    private WeaponDeck weaponDeck = new WeaponDeck();
    private PowerUpDeck powerupDeck = new PowerUpDeck();
    private AmmoDeck ammoDeck = new AmmoDeck();
    private Square[][] squares = new Square[3][4];
    private int skulls;
    private int gameSize;
    private List<List<PlayerColor>> killShotTrack = new ArrayList<>();
    private static final int MAX_SKULLS = 8;
    private static final int CARDS_IN_SHOPS = 3;

    public GameBoard(int gameLength, int gameSize) {
        this.skulls = gameLength;
        this.gameSize = gameSize;
        for(int i = 0; i < MAX_SKULLS - skulls; i++)
            killShotTrack.add(new ArrayList<>());
        ArrayList<WeaponCard> blueShop = new ArrayList<>();
        ArrayList<WeaponCard> redShop = new ArrayList<>();
        ArrayList<WeaponCard> yellowShop = new ArrayList<>();
        for(int i = 0; i < CARDS_IN_SHOPS; i++) {
            blueShop.add(weaponDeck.draw());
            redShop.add(weaponDeck.draw());
            yellowShop.add(weaponDeck.draw());
        }
        if(gameSize == 10) {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, ROOM}, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(0, 2, BLUE, new SquareBorder[]{NOTHING, DOOR, ROOM, NOTHING}, blueShop);
            squares[0][3] = null;
            squares[1][0] = new SpawnAndShopSquare(1, 0, RED, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, redShop);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{WALL, DOOR, ROOM, ROOM}, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, WALL, ROOM, DOOR}, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{NOTHING, ROOM, DOOR, NOTHING}, ammoDeck.draw());
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{WALL, NOTHING, ROOM, DOOR}, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(2, 3, YELLOW, new SquareBorder[]{ROOM, NOTHING, DOOR, NOTHING}, yellowShop);
        }
        else if (gameSize == 11) {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, DOOR, NOTHING, ROOM}, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, WALL, ROOM, ROOM}, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(0, 2, BLUE, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, blueShop);
            squares[0][3] = new AmmoSquare(0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck.draw());
            squares[1][0] = new SpawnAndShopSquare(1, 0, RED, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, redShop);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{WALL, DOOR, ROOM, WALL}, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck.draw());
            squares[2][0] = null;
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, NOTHING, DOOR}, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(2, 3, YELLOW, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, yellowShop);
        }
        else {
            squares[0][0] = new AmmoSquare(0, 0, new SquareBorder[]{NOTHING, ROOM, NOTHING, DOOR}, ammoDeck.draw());
            squares[0][1] = new AmmoSquare(0, 1, new SquareBorder[]{NOTHING, DOOR, DOOR, ROOM}, ammoDeck.draw());
            squares[0][2] = new SpawnAndShopSquare(0, 2, BLUE, new SquareBorder[]{NOTHING, DOOR, ROOM, DOOR}, blueShop);
            squares[0][3] = new AmmoSquare(0, 3, new SquareBorder[]{NOTHING, DOOR, DOOR, NOTHING}, ammoDeck.draw());
            squares[1][0] = new SpawnAndShopSquare(1, 0, RED, new SquareBorder[]{ROOM, DOOR, NOTHING, WALL}, redShop);
            squares[1][1] = new AmmoSquare(1, 1, new SquareBorder[]{DOOR, DOOR, WALL, WALL}, ammoDeck.draw());
            squares[1][2] = new AmmoSquare(1, 2, new SquareBorder[]{DOOR, ROOM, WALL, ROOM}, ammoDeck.draw());
            squares[1][3] = new AmmoSquare(1, 3, new SquareBorder[]{DOOR, ROOM, ROOM, NOTHING}, ammoDeck.draw());
            squares[2][0] = new AmmoSquare(2, 0, new SquareBorder[]{DOOR, NOTHING, NOTHING, ROOM}, ammoDeck.draw());
            squares[2][1] = new AmmoSquare(2, 1, new SquareBorder[]{DOOR, NOTHING, ROOM, DOOR}, ammoDeck.draw());
            squares[2][2] = new AmmoSquare(2, 2, new SquareBorder[]{ROOM, NOTHING, DOOR, ROOM}, ammoDeck.draw());
            squares[2][3] = new SpawnAndShopSquare(2, 3, YELLOW, new SquareBorder[]{ROOM, NOTHING, ROOM, NOTHING}, yellowShop);
        }
    }

    public void addKillShotTrack(List<PlayerColor> newKillShot) {
        if(killShotTrack.size() < MAX_SKULLS)
            killShotTrack.add(newKillShot);
        else
            killShotTrack.get(MAX_SKULLS - 1).addAll(newKillShot);
    }

    public List<Square> getSquares(Player player, int dist) {
        int j = 1;

        if(dist == -1)
            return Collections.emptyList();

        if(dist == 0)
            return Collections.singletonList(player.getCurrentSquare());

        //Add all valid squares of distance 1
        List<Square> tempSquare = distanceOneSquares(player.getCurrentSquare());

        // For each number beyond 1...
        for (int i = 1; i < dist; i++) {
            int temp = tempSquare.size();
            //...add all valid squares of distance 1 not already added
            for (; j < temp; j++) {
                if ((tempSquare.get(j).getNorth() == DOOR || tempSquare.get(j).getNorth() == ROOM)
                        && !tempSquare.contains(squares[tempSquare.get(j).getY() - 1][tempSquare.get(j).getX()]))
                    tempSquare.add(squares[tempSquare.get(j).getY() - 1][tempSquare.get(j).getX()]);
                if ((tempSquare.get(j).getSouth() == DOOR || tempSquare.get(j).getSouth() == ROOM)
                        && !tempSquare.contains(squares[tempSquare.get(j).getY() + 1][tempSquare.get(j).getX()]))
                    tempSquare.add(squares[tempSquare.get(j).getY() + 1][tempSquare.get(j).getX()]);
                if ((tempSquare.get(j).getWest() == DOOR || tempSquare.get(j).getWest() == ROOM)
                        && !tempSquare.contains(squares[tempSquare.get(j).getY()][tempSquare.get(j).getX() - 1]))
                    tempSquare.add(squares[tempSquare.get(j).getY()][tempSquare.get(j).getX() - 1]);
                if ((tempSquare.get(j).getEast() == DOOR || tempSquare.get(j).getEast() == ROOM)
                        && !tempSquare.contains(squares[tempSquare.get(j).getY()][tempSquare.get(j).getX() + 1]))
                    tempSquare.add(squares[tempSquare.get(j).getY()][tempSquare.get(j).getX() + 1]);
            }
        }
        return tempSquare;
    }

    private List<Square> distanceOneSquares(Square square) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(square);

        if (tempSquare.get(0).getNorth() == DOOR || tempSquare.get(0).getNorth() == ROOM)
            tempSquare.add(squares[tempSquare.get(0).getY() - 1][tempSquare.get(0).getX()]);
        if (tempSquare.get(0).getSouth() == DOOR || tempSquare.get(0).getSouth() == ROOM)
            tempSquare.add(squares[tempSquare.get(0).getY() + 1][tempSquare.get(0).getX()]);
        if (tempSquare.get(0).getWest() == DOOR || tempSquare.get(0).getWest() == ROOM)
            tempSquare.add(squares[tempSquare.get(0).getY()][tempSquare.get(0).getX() - 1]);
        if (tempSquare.get(0).getEast() == DOOR || tempSquare.get(0).getEast() == ROOM)
            tempSquare.add(squares[tempSquare.get(0).getY()][tempSquare.get(0).getX() + 1]);

        return tempSquare;
    }

    public List<Square> getInRangeSquares(Player player) {
        //Add rooms near the player's current square...
        List<Square> tempSquare = distanceOneDoors(player.getCurrentSquare());

        //...and add the other squares of those room
        for(int i = 0; i < tempSquare.size(); i++) {
            if(tempSquare.get(i).getNorth() == ROOM && !tempSquare.contains(squares[tempSquare.get(i).getY() - 1][tempSquare.get(i).getX()]))
                tempSquare.add(squares[tempSquare.get(i).getY() - 1][tempSquare.get(i).getX()]);
            if(tempSquare.get(i).getSouth() == ROOM && !tempSquare.contains(squares[tempSquare.get(i).getY() + 1][tempSquare.get(i).getX()]))
                tempSquare.add(squares[tempSquare.get(i).getY() + 1][tempSquare.get(i).getX()]);
            if(tempSquare.get(i).getWest() == ROOM && !tempSquare.contains(squares[tempSquare.get(i).getY()][tempSquare.get(i).getX() - 1]))
                tempSquare.add(squares[tempSquare.get(i).getY()][tempSquare.get(i).getX() - 1]);
            if(tempSquare.get(i).getEast() == ROOM && !tempSquare.contains(squares[tempSquare.get(i).getY()][tempSquare.get(i).getX() + 1]))
                tempSquare.add(squares[tempSquare.get(i).getY()][tempSquare.get(i).getX() + 1]);
        }
        return tempSquare;
    }

    private List<Square> distanceOneDoors(Square square) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(square);

        if(tempSquare.get(0).getNorth() == DOOR)
            tempSquare.add(squares[tempSquare.get(0).getY() - 1][tempSquare.get(0).getX()]);
        if(tempSquare.get(0).getSouth() == DOOR)
            tempSquare.add(squares[tempSquare.get(0).getY() + 1][tempSquare.get(0).getX()]);
        if(tempSquare.get(0).getWest() == DOOR)
            tempSquare.add(squares[tempSquare.get(0).getY()][tempSquare.get(0).getX() - 1]);
        if(tempSquare.get(0).getEast() == DOOR)
            tempSquare.add(squares[tempSquare.get(0).getY()][tempSquare.get(0).getX() + 1]);

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
        List<Square> tempCloseSquare = getSquares(player, maxDistance);
        List<Player> tempPlayers = new ArrayList<>();
        for(Square s : tempSquare) {
            if (tempCloseSquare.contains(s))
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
            if(!tempNearSquare.contains(s) && (s.getPlayers().size() > 1 || (!s.getPlayers().contains(player) && s.getPlayers().size() > 0)))
                tempFarSquares.add(s);
        return tempFarSquares;
    }

    public List<Square> getNearSquares(Player player, int maxDistance) {
        List<Square> tempSquare = getInRangeSquares(player);
        List<Square> tempFarSquares = getSquares(player, maxDistance);
        List<Square> tempNearSquare = new ArrayList<>();
        for(Square s : tempSquare)
            if(tempFarSquares.contains(s) && (s.getPlayers().size() > 1 || (!s.getPlayers().contains(player) && s.getPlayers().size() > 0)))
                tempNearSquare.add(s);
        return tempNearSquare;
    }

    public List<Square> getOtherVisibleRoom(Player player) {
        List<Square> tempSquare = new ArrayList<>();
        if(player.getCurrentSquare().getNorth() == ROOM) {
            tempSquare.add(squares[player.getCurrentSquare().getX()][player.getCurrentSquare().getY() + 1]);
        }

        if(player.getCurrentSquare().getEast() == ROOM) {
            tempSquare.add(squares[player.getCurrentSquare().getX() + 1][player.getCurrentSquare().getY()]);
        }

        if(player.getCurrentSquare().getSouth() == ROOM) {
            tempSquare.add(squares[player.getCurrentSquare().getX()][player.getCurrentSquare().getY() - 1]);
        }

        if(player.getCurrentSquare().getWest() == ROOM) {
            tempSquare.add(squares[player.getCurrentSquare().getX() - 1][player.getCurrentSquare().getY()]);
        }

        return tempSquare;
    }

    public List<Square> getFurnaceVisibleSquares(Player player) {
        List<Square> tempSquare = new ArrayList<>();
        tempSquare.add(squares[player.getCurrentSquare().getX()][player.getCurrentSquare().getY() + 1]);
        tempSquare.add(squares[player.getCurrentSquare().getX() + 1][player.getCurrentSquare().getY()]);
        tempSquare.add(squares[player.getCurrentSquare().getX() - 1][player.getCurrentSquare().getY()]);
        tempSquare.add(squares[player.getCurrentSquare().getX() - 1][player.getCurrentSquare().getY()]);

        return tempSquare;
    }

    public List<Player> getNonVisiblePlayers(Player player) {
        List<Player> tempInRangePlayers = getInRangePlayers(player);
        List<Player> tempPlayers = new ArrayList<>();
        for(Player p : players) {
            if(!tempPlayers.contains(p)) {
                tempPlayers.add(p);
            }
        }

        return tempPlayers;
    }

    public void setPlayers(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public PowerUpDeck getPowerUpDeck() {
        return powerupDeck;
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
}
