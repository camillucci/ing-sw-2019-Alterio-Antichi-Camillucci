package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;

import java.util.*;

/**
 * This class is used to group all players who are playing on the same match. It's the main part of the controller and
 * the one that communicates with the server to receive all the decisions the players take.
 */
public class Room
{
    /**
     * Event that other classes can subscribe to. The event is invoked when the before match countdown starts.
     */
    public final IEvent<Room, Integer> timerStartEvent = new Event<>();

    /**
     * Event that other classes can subscribe to. The event is invoked when the before match countdown reaches the end.
     */
    public final IEvent<Room, Integer> timerTickEvent = new Event<>();

    /**
     * Event that other classes can subscribe to. The event is invoked if the before match countdown is already started
     * and the number of connected players goes under 3.
     */
    public final IEvent<Room, Integer> timerStopEvent = new Event<>();

    /**
     * Event that other classes can subscribe to. The event is invoked when a new player enters the room.
     */
    public final IEvent<Room, String> newPlayerEvent = new Event<>();

    /**
     * Event that other classes can subscribe to. The event is invoked when a player who's in the room disconnects.
     */
    public final IEvent<Room, String> playerDisconnectedEvent = new Event<>();
    public final IEvent<Room, ModelEventArgs> modelUpdatedEvent = new Event<>();

    /**
     * Integer representing the timeout value
     */
    private static final int TIMEOUT = 2;

    /**
     * Integer representing the period value
     */
    private static final int PERIOD = 1;

    /**
     * List of colors representing all the colors of the players who have already chosen one.
     */
    private List<PlayerColor> playerColors = new ArrayList<>();

    /**
     * List of all the available colors
     */
    private List<PlayerColor> availableColors = new ArrayList<>();

    /**
     * List of all names relative to the players already present in the room
     */
    private List<String> playerNames = new ArrayList<>();

    /**
     * List of all players who joined the room and later on disconnected
     */
    private List<String> disconnectedPlayers = new ArrayList<>();
    private List<String> pendingPlayers = new ArrayList<>();
    private List<String> readyPlayers = new ArrayList<>();

    /**
     * Number of skulls relative to the match the room is managing
     */
    private int gameLength;

    /**
     * Integer that represents the map type relative to the match the room is managing
     */
    private int gameSize;

    /**
     * Match relative to this room
     */
    private Match match;
    private RoomTimer timer = new RoomTimer(TIMEOUT, PERIOD);

    /**
     * Name of the player who logged into the room first. (Some of the match specifics are asked to the host).
     */
    private String hostName;

    /**
     * Boolean representing whether the match is started.
     */
    private boolean matchStarting = false;

    public Room() {
        availableColors.addAll(Arrays.asList(PlayerColor.values()));
        setupEvents();
    }

    /**
     * Returns the match state
     * @return Whether the match has started or not.
     */
    public boolean isMatchStarted() {
        return matchStarting;
    }

    /**
     * Sets the events relative to timeouts and players disconnecting.
     */
    private void setupEvents()
    {
        timer.timerTickEvent.addEventHandler((a, timeElapsed) -> ((Event<Room, Integer>)timerTickEvent).invoke(this, TIMEOUT - timeElapsed));
        timer.timeoutEvent.addEventHandler((a, b) -> onTimeout());
        playerDisconnectedEvent.addTmpEventHandler((a, name) -> disconnectedPlayers.add(name));
    }

    /**
     * When the timeout event is invoked, this method checks whether there are enough players to start the game. In
     * case there aren't, the timer is stopped; otherwise the matchStarting paramter is set to true and the match starts.
     */
    private synchronized void onTimeout(){
        int tot = readyPlayers.size() + pendingPlayers.size();
        if(tot < 3){
            ((Event<Room, Integer>)timerStopEvent).invoke(this, TIMEOUT);
            return;
        }

        matchStarting = true;
        if(pendingPlayers.isEmpty())
            startMatch();
    }

    /**
     * Starts a match using the parameters chosen by the host and the parameters relative to the players already present
     * in the room.
     */
    private synchronized void startMatch(){
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        match.newActionsEvent.addEventHandler(this::onNewActions);
        match.start();
    }

    private void onNewActions(Player player, List<Action> actions)
    {
        for(Player p : match.getPlayers())
            ((Event<Room, ModelEventArgs>)modelUpdatedEvent).invoke(this, new ModelEventArgs(new MatchSnapshot(match, p), p.name, new RemoteActionsHandler(player, actions)));
    }

    public void reconnect(String playerName){
        //TODO
    }

    /**
     * Adds the player to the pendingPlayers list and removes their color from the availableColors list, while adding
     * it to the playersColors list. Also, if the player is the first one to join the room, they are made host.
     * @param color Color chosen by the added player
     * @param playerName Name chosen by the added player
     */
    public synchronized void addPlayer(String color, String playerName) throws MatchStartingException, NotAvailableColorException {
        if(matchStarting || availableColors.isEmpty())
            throw new MatchStartingException();

        int index = getAvailableColors().indexOf(color);

        if(index == -1)
            throw new NotAvailableColorException();

        pendingPlayers.add(playerName);
        playerColors.add(availableColors.get(index));
        availableColors.remove(index);
        playerNames.add(playerName);
        if(playerNames.size() == 1)
            hostName = playerName;
    }

    /**
     * Checks whether the name corresponds to the name of the room's host
     * @param name Name that needs to be confronted with the name of the host
     * @return True if the name is the same of the host's one, false otherwise.
     */
    public boolean isHost(String name) {
        return name.equals(hostName);
    }

    /**
     * Invokes the newPlayerEvent using that player's name as a parameter
     * @param name Name of the new player who joined the room
     */
    private void newPlayer(String name){
        ((Event<Room, String>)newPlayerEvent).invoke(this, name);
    }

    /**
     * Returns list with all names of the players in the room
     * @return List with all names of the players in the room
     */
    public synchronized List<String> getPlayerNames(){
        return new ArrayList<>(playerNames);
    }

    /**
     * The player with the same name as the one in the parameters is removed from the pendingPlayers and
     * playerNames lists. Plus their color is added to the list of available colors and removed from the
     * playerColors list.
     * @param name Name of the removed player
     */
    private synchronized void removePlayer(String name){
        int index = playerNames.indexOf(name);
        pendingPlayers.remove(name);
        playerNames.remove(index);
        availableColors.add(playerColors.get(index));
        playerColors.remove(index);
    }

    /**
     * A new player is created using the name gotten as parameter and that player is removed from the pendingPlayers
     * list, while added to the redyPlayers one. The readyCounter is incremented. If all the pending players have
     * been removed from the list and the match is ready to start, then a new match is created. Otherwise,
     * depending on the number of ready players either the timer is started (3 players) or the match is started
     * (5 players)
     * @param playerName
     */
    public synchronized void notifyPlayerReady(String playerName)
    {
        newPlayer(playerName);
        pendingPlayers.remove(playerName);
        readyPlayers.add(playerName);
        int readyCounter = readyPlayers.size();
        if(pendingPlayers.isEmpty() && matchStarting)
            startMatch();
        else if (readyCounter == 3) {
            timer.start();
            ((Event<Room, Integer>) timerStartEvent).invoke(this, TIMEOUT);
        } else if (readyCounter == 5) {
            timer.stop();
            startMatch();
        }
    }

    /**
     * If the match is not started yet, the player is removed from then room. Then the playerDisconnectedEvent is
     * invoke, regardless of match starting or not.
     * @param name Name of the disconnected player
     */
    public void notifyPlayerDisconnected(String name){
        if(!matchStarting) {
            removePlayer(name);
            readyPlayers.remove(name);
        }
        ((Event<Room, String>)playerDisconnectedEvent).invoke(this, name);
    }

    /**
     * Returns a list of available colors from which the player can choose. This method is synchronized in order
     * to avoid the bad case scenario where to players on the same room are choosing a color.
     * @return A list of available colors
     */
    public synchronized List<String> getAvailableColors() throws MatchStartingException
    {
        if(availableColors.isEmpty())
            throw new MatchStartingException();

        ArrayList<String> colors = new ArrayList<>();
        for (PlayerColor pc : availableColors)
            colors.add(pc.name());
        return colors;
    }

    public synchronized boolean isJoinable(){
        return !availableColors.isEmpty() && !matchStarting;
    }

    /**
     * The number of skulls is set based on host's decision
     * @param gameLength Number of skulls the match is going to be made of
     */
    public synchronized void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    /**
     * The map type is set based on host's decision
     * @param gameSize Map type the match is going to be played on.
     */
    public synchronized void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public class MatchStartingException extends Exception {}
    public class NotAvailableColorException extends Exception {}
    public class ModelEventArgs
    {
        public final MatchSnapshot matchSnapshot;
        public final RemoteActionsHandler actionsHandler;
        public final String playerName;
        public ModelEventArgs(MatchSnapshot matchSnapshot, String playerName, RemoteActionsHandler actionsHandler)
        {
            this.matchSnapshot = matchSnapshot;
            this.actionsHandler = actionsHandler;
            this.playerName = playerName;
        }
    }
}
