package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.ServerRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used as a mediator between the server and the model side. It takes users decisions from the server
 * and communicates them to the model, which adjusts the board state accordingly.
 */
public class Controller {
    /**
     * List of rooms in which all the players are contained. Every room has the set of players who are in the same game
     * (from 3 to 5).
     */
    private List<Room> lobby = new ArrayList<>();

    /**
     * List of players who want to enter a room. It's used in case one of the user disconnects before they could get
     * in a room.
     */
    private List<String> joiningPlayers = new ArrayList<>();

    /**
     * Integer representing the login timeout value
     */
    private final int loginTimer;

    private Logger logger = Logger.getLogger("Controller");

    /**
     * Integer that represents the amount of seconds it takes for the turn timer to reach 0.
     */
    private final int turnTimer;

    /**
     * Constructor. Initializes the room list by creating an empty room.
     * @param loginTimer timer used to as a reference to start the game when it reaches zero
     * @param turnTimer timer used to as a reference to end the turn when it reaches zero
     */
    public Controller(int loginTimer, int turnTimer){
        this.loginTimer = loginTimer;
        this.turnTimer = turnTimer;
        newRoom(loginTimer, turnTimer);
    }

    /**
     * Creates a new room and subscribes to the newPlayerEvent. When the event is invoked, the player is removed from
     * joiningPlayers list. Also adds the new room to the previously existing list.
     * @param loginTimer timer used to as a reference to start the game when it reaches zero
     * @param turnTimer timer used to as a reference to end the turn when it reaches zero
     * @return Newly created room
     */
    private Room newRoom(int loginTimer, int turnTimer) {
        Room room = new Room(loginTimer, turnTimer);
        room.newPlayerEvent.addEventHandler((a, name) -> joiningPlayers.remove(name));

        room.roomClosedEvent.addEventHandler(this::onRoomClosed);
        lobby.add(room);
        return room;
    }

    /**
     * Method called when a room is no longer needed due to the end of a match
     * @param r Room that needs to be removed from the lobby list
     * @param id Integer used to identify the room when notifying the server
     */
    private void onRoomClosed(Room r, int id){
        lobby.remove(r);
        logger.log(Level.INFO, () -> "room number " + id + " closed");
    }

    /**
     * Calculates which is the available room at the moment. If there isn't a new one is created.
     * @return The available room in which new joining players are gonna be put.
     */
    public synchronized Room getAvailableRoom() {
        if(lobby.isEmpty())
            return newRoom(loginTimer, turnTimer);
        Room ret = lobby.get(lobby.size()-1);
        if(ret.isJoinable())
            return ret;
        return newRoom(loginTimer, turnTimer);
    }

    /**
     * Checks if the name gotten as a parameter is already taken by one of the previously logged in players.
     * The method is synchronized in order to avoid the bad case scenario where two players try to log in at the same
     * time and choose the same name.
     * @param name Name chosen by the user who's trying to enter a room.
     * @return False in case the name is already been taken. True otherwise.
     */
    public synchronized boolean newPlayer(String name) {
        for (Room room : lobby) {
            if (room.getPlayerNames().contains(name))
                return false;
        }
        if(joiningPlayers.contains(name))
            return false;
        joiningPlayers.add(name);
        return true;
    }

    /**
     * Checks whether the name gotten as input is associated with a player preciously in game and disconnected. If that's
     * the case, the room is notified and the player is put back into the list of players currently playing the game
     * @param name String that represents the name associated with the newly connected player
     * @return True if the name was associated with a player that reconnected. False if it's the first time a player
     *         with that name joins the game.
     */
    public synchronized Room checkReconnected(String name) {
        for (Room room : lobby) {
            if (room.getDisconnectedPlayers().contains(name)) {
                room.reconnectedPlayer(name);
                return room;
            }
        }
        return null;
    }

    /**
     * Checks if the disconnected player was already in a room. If that's the case, said room is notified of the
     * player's disconnection; otherwise the player is removed from the joiningPlayers list (in case they were in it).
     * @param name Name of the player who's lost connection
     */
    public synchronized void notifyPlayerDisconnected(String name){
        if(name == null) // name not registered
            return;

        if(joiningPlayers.contains(name)) {
            joiningPlayers.remove(name);
            return;
        }

        for(Room room : lobby)
            if(room.getPlayerNames().contains(name)) {
                room.notifyPlayerDisconnected(name);

                return;
            }
        throw new ServerRuntimeException("Disconnected player doesn't exist");
    }
}
