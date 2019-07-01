package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
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
     * Event that other classes can subscribe to. The event is invoked every PERIOD interval of time, after that the timer is started.
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
     * todo
     */
    public final IEvent<Room, String> onEndMatchEvent = new Event<>();

    public final IEvent<Room, String[][]> onEndMatchScoreEvent = new Event<>();

    public final IEvent<Room, String> turnTimeoutEvent = new Event<>();

    /**
     * Integer representing the timeout value
     */
    private static final int LOGIN_TIMEOUT = 1;
    private static final int TURN_TIMEOUT = 1000000000;

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
     * List of all names relative to the players present in the room and playing in game.
     */
    private List<String> playerNames = new ArrayList<>();

    /**
     * List of all players who joined the room and later on disconnected
     */
    private List<String> disconnectedPlayers = new ArrayList<>();
    private List<String> pendingPlayers = new ArrayList<>();
    private List<String> readyPlayers = new ArrayList<>();

    /**
     * Number of skulls relative to the match this room is managing
     */
    private int gameLength;

    /**
     * Integer that represents the map type relative to the match the room is managing
     */
    private int gameSize;

    /**
     * Match associated with this room
     */
    private Match match;
    private RoomTimer timer;

    /**
     * Name of the player who logged into the room first. (Some of the match specifics are asked to the host).
     */
    private String hostName;

    /**
     * Boolean representing whether the match is started.
     */
    private boolean matchStarting = false;
    private boolean matchStarted = false;
    private final static int MIN_PLAYERS = 2;
    private ModelEventArgs curModel;


    public Room() {
        availableColors.addAll(Arrays.asList(PlayerColor.values()));
        newLoginTimer();
    }

    /**
     * Returns the match state
     * @return Whether the match has started or not.
     */
    public boolean isMatchStarted() {
        return pendingPlayers.isEmpty() && readyPlayers.size() >= MIN_PLAYERS;
    }

    public boolean isTheMatchStarted() {
        return matchStarted;
    }

    /**
     * Sets the events relative to timeouts and players disconnecting.
     */

    private void newLoginTimer(){
        timer = new RoomTimer(LOGIN_TIMEOUT, PERIOD);
        timer.timerTickEvent.addEventHandler((a, timeElapsed) -> ((Event<Room, Integer>)timerTickEvent).invoke(this, LOGIN_TIMEOUT - timeElapsed));
        timer.timeoutEvent.addEventHandler((a, b) -> onTimeout());
    }

    /**
     * When the timeout event is invoked, this method checks whether there are enough players to start the game. In
     * case there aren't, the timer is stopped; otherwise the matchStarting parameter is set to true and the match starts.
     */
    private synchronized void onTimeout(){
        int tot = readyPlayers.size() + pendingPlayers.size();
        if(tot < MIN_PLAYERS){
            stopTimer();
            newLoginTimer();
            return;
        }

        matchStarting = true;
        if(pendingPlayers.isEmpty())
            startMatch();
    }


    private void stopTimer(){
        matchStarting = false;
        timer.stop();
        ((Event<Room, Integer>)timerStopEvent).invoke(this, LOGIN_TIMEOUT);
    }

    private synchronized void onTurnTimeout(){
        if(timer.getElapsed() >= TURN_TIMEOUT-1)
            onTurnTimeout_Sync();
    }

    private void onTurnTimeout_Sync(){
        ((Event<Room, String>) turnTimeoutEvent).invoke(this, match.getPlayer().name);
        resetTurnTimer();
        match.skipTurn();
        createTurnTimer();
    }

    public synchronized void setClientIdle(String name, boolean isAlive) throws TurnTimeoutException {
        if(!match.getPlayer().name.equals(name))
            return;

        if(isAlive)
            if(getTurnTimeout())
                throw new TurnTimeoutException();
            else
                resetTurnTimer();
        else
            onTurnTimeout_Sync();
    }

    public boolean getTurnTimeout(){
        return timer != null && timer.getElapsed() >= TURN_TIMEOUT-1;
    }

    /**
     * Starts a match using the parameters chosen by the host and the parameters relative to the players already present
     * in the room.
     */
    private synchronized void startMatch(){
        matchStarted = true;
        match = new Match(playerNames, playerColors, gameLength, gameSize);
        match.newActionsEvent.addEventHandler(this::onNewActions);
        match.start();
        match.endMatchEvent.addEventHandler((match, players) -> onMatchEnd());
        createTurnTimer();
    }

    private void onNewActions(Player player, List<Action> actions)
    {
        if(disconnectedPlayers.contains(match.getPlayer().name)) {
            onTurnTimeout_Sync();
            return;
        }
        resetTurnTimer();
        for(Player p : match.getPlayers())
            ((Event<Room, ModelEventArgs>)modelUpdatedEvent).invoke(this, new ModelEventArgs(new MatchSnapshot(match, p), p.name, new RemoteActionsHandler(player, actions)));
    }

    private synchronized void resetTurnTimer(){
        timer.reset();
    }

    private void createTurnTimer(){
        this.timer = new RoomTimer(TURN_TIMEOUT, PERIOD);
        timer.timeoutEvent.addEventHandler((a,b) -> onTurnTimeout());
    }

    public synchronized void reconnect(String playerName){
        disconnectedPlayers.remove(playerName);
    }

    public synchronized void onPlayerTimeout(){

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
        newPlayer(playerName);
    }

    public synchronized void reconnectedPlayer(String name) {
        if(isTheMatchStarted())
            disconnectedPlayers.remove(name);
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
        readyPlayers.remove(name);
        pendingPlayers.remove(name);
        playerNames.remove(index);
        availableColors.add(playerColors.get(index));
        playerColors.remove(index);
        if(pendingPlayers.size() + readyPlayers.size() < MIN_PLAYERS) {
            stopTimer();
            newLoginTimer();
        }
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
        pendingPlayers.remove(playerName);
        readyPlayers.add(playerName);
        int readyCounter = readyPlayers.size();
        if(pendingPlayers.isEmpty() && matchStarting)
            startMatch();
        else if (readyCounter == MIN_PLAYERS) {
            timer.start();
            ((Event<Room, Integer>) timerStartEvent).invoke(this, LOGIN_TIMEOUT);
        } else if (readyCounter == 5) {
            matchStarting = true;
            timer.stop();
            startMatch();
        }
    }

    /**
     * If the match is not started yet, the player is removed from then room. Then the playerDisconnectedEvent is
     * invoked, regardless of match starting or not.
     * @param name Name of the disconnected player
     */
    public void notifyPlayerDisconnected(String name){
        if(!matchStarting)
            removePlayer(name);
        else {
            disconnectedPlayers.add(name);
            if(playerNames.size() - disconnectedPlayers.size() < MIN_PLAYERS)
                onMatchEnd();
            else if(match.getPlayer().name.equals(name))
                onTurnTimeout_Sync();
        }
        ((Event<Room, String>)playerDisconnectedEvent).invoke(this, name);

        //todo check if the else is appropriate
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

    //todo add possible exceptions
    public List<String> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    /**
     * Given a player name, this method returns the list of all the players currently playing except the one gotten as
     * input.
     * @param player Name of the player to be excluded from the returned list
     * @return List of strings representing the names of all players except the one in the input parameters
     */
    //todo add possible exceptions
    public List<String> getOtherPlayers(String player) {
        List<String> temp = new ArrayList<>(playerNames);
        temp.remove(player);
        for(String p : disconnectedPlayers)
            temp.remove(p);
        return temp;
    }

    //todo add possible exceptions
    public PlayerColor getPlayerColor(String name) {
        return playerColors.get(playerNames.indexOf(name));
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

    /**
     * This method is called when the match reaches an end and associates to every player a number indicative of the
     * score they got by playing the game. Then it creates a leader board based on the amount of points that every
     * player has acquired.
     * @return The leader board that represents the final ladder based on player points.
     */
    private String[][] calculateScore() {
        String[][] scoreBoard = new String[match.getPlayers().size()][2];
        for(int i = 0; i < match.getPlayers().size(); i++) {
            scoreBoard[i][0] = match.getPlayers().get(i).name;
            scoreBoard[i][1] = Integer.toString(match.getPlayers().get(i).getPoints());
        }
        for(int i = 0; i < match.getPlayers().size(); i++)
            for(int j = i; j < match.getPlayers().size(); j++)
                if(Integer.parseInt(scoreBoard[j][1]) > Integer.parseInt(scoreBoard[i][1])) {
                    String[] temp = new String[]{scoreBoard[i][0], scoreBoard[i][1]};
                    scoreBoard[i][0] = scoreBoard[j][0];
                    scoreBoard[i][1] = scoreBoard[j][1];
                    scoreBoard[j][0] = temp[0];
                    scoreBoard[j][1] = temp[1];
                }
        return scoreBoard;
    }

    /**
     * Calculates who's the winning player by finding the player with the maximum number of points
     * @return Winning player's name
     */
    private String declareWinner() {
        Player player = match.getPlayers().get(0);
        for(int i = 1; i < match.getPlayers().size(); i++)
            if(match.getPlayers().get(i).getPoints() > player.getPoints() || (match.getPlayers().get(i).getPoints() == player.getPoints() &&
                    countKillShotTrack(match.getPlayers().get(i)) > countKillShotTrack(player)))
                player = match.getPlayers().get(i);
        return player.name;
    }

    /**
     * Calculates the kill shot Track associated with a specific player by confronting their color with color of all the
     * drops present on the game board kill shot track.
     * @param player Player whose kill shot Track is going to be calculated.
     * @return The kill shot Track associated with the player.
     */
    private int countKillShotTrack(Player player) {
        int temp = 0;
        for(List<PlayerColor> list : match.getGameBoard().getKillShotTrack())
            for(PlayerColor playerColor : list)
                if(playerColor == player.color)
                    temp++;
        return temp;
    }

    private void onMatchEnd() {
        String[][] scoreBoard = calculateScore();
        String winnerName = declareWinner();
        ((Event<Room, String>)onEndMatchEvent).invoke(this, winnerName);
        ((Event<Room, String[][]>)onEndMatchScoreEvent).invoke(this, scoreBoard);
    }

    public ModelEventArgs getCurModel(String name) {
        Player player;
        for(Player p : match.getPlayers())
            if(p.name.equals(name))
                return new ModelEventArgs(new MatchSnapshot(match, p), p.name, new RemoteActionsHandler(match.getPlayer(), match.getActions()));
        throw new RuntimeException("Player does not exist in this match");
    }

    public class TurnTimeoutException extends Exception{}
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
