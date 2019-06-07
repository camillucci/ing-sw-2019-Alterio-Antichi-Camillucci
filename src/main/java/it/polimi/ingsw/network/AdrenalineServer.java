package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public abstract class AdrenalineServer implements IAdrenalineServer
{
    private final Controller controller;
    protected String name;
    protected boolean isHost;
    private Room joinedRoom;
    private List<String> availableColors;
    private List<String> otherPlayers = new ArrayList<>();
    protected Bottleneck bottleneck = new Bottleneck();
    protected Match match;
    private Player player;
    private int playerIndex;
    protected RemoteActionsHandler remoteActionsHandler;
    private BiConsumer<Room, Integer> timerStartEventHandler = (a, timeout) -> bottleneck.tryDo( () -> sendMessage(timerStartMessage(timeout)));
    private BiConsumer<Room, Integer> timerTickEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(timerTickMessage(timeLeft)));
    private BiConsumer<Room, Integer> timerStopEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(TIMER_STOPPED_MESSAGE));
    private BiConsumer<Room, String> newPlayerEventHandler = (a, name) -> notifyPlayer(name);
    private BiConsumer<Room, String> playerDisconnectedEventHandler = (a, name) -> notifyPlayerDisconnected(name);
    private BiConsumer<Room, Match> matchStartedEventHandler = (a, match) -> bottleneck.tryDo( () -> onMatchStarted(match));
    private BiConsumer<Player, List<Action>> onNewActionsEventHandler = (a,b) -> bottleneck.tryDo( () -> onModelChanged(match));
    protected static final int PING_PERIOD = 1; // 1 millisecond to test synchronization todo change in final version

    public AdrenalineServer(Controller controller){
        this.controller = controller;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
    }

    protected void onExceptionGenerated(Exception e){
        removeEvents();
        controller.notifyPlayerDisconnected(name);
    }
    private synchronized void notifyPlayerDisconnected(String name){
        otherPlayers.remove(name);
        bottleneck.tryDo(() -> sendMessage(playerDisconnectedMessage(name)));
    }

    protected abstract void startPinging();
    protected abstract void stopPinging();

    @Override
    public boolean setName(String name)
    {
        if(controller.newPlayer(name)) {
            this.name = name;
            return true;
        }
        return false;
    }
    @Override
    public List<String> availableColors()
    {
        try {
            joinedRoom = controller.getAvailableRoom();
            availableColors = joinedRoom.getAvailableColors();
        }
        catch (Room.MatchStartingException e) {
            return availableColors();
        }
        return availableColors;
    }

    @Override
    public boolean setColor(int colorIndex) {
        try
        {
            joinedRoom.addPlayer(availableColors.get(colorIndex), name);
            this.isHost = isHost();
            return true;
        } catch (Room.MatchStartingException | Room.NotAvailableColorException e) {
            return false;
        }
    }

    @Override
    public boolean isHost() {
        return joinedRoom.isHost(name);
    }

    @Override
    public void setGameLength(int gameLength) {
        joinedRoom.setGameLength(gameLength);
    }
    @Override
    public void setGameMap(int choice) {
        joinedRoom.setGameSize(choice);
    }

    private synchronized void notifyPlayer(String name){
        if(otherPlayers.contains(name))
            return;
        otherPlayers.add(name);
        bottleneck.tryDo(() -> sendMessage(newPlayerMessage(name)));
    }

    @Override
    public void ready() {
        joinedRoom.notifyPlayerReady(name);
        setupRoomEvents();
        for(String name : joinedRoom.getPlayerNames())
            notifyPlayer(name);
    }

    private void onMatchStarted(Match match) throws IOException, ClassNotFoundException {
        this.match = match;
        setupMatchEvents();
        List<Player> players = match.getPlayers();
        for(int i=0; i < players.size(); i++)
            if(players.get(i).name.equals(this.name)) {
                playerIndex = i;
                this.player = players.get(playerIndex);
            }
        createActionHandler(player);
        sendMessage(MATCH_STARTING_MESSAGE);
        onModelChanged(match);
        stopPinging();
    }
    private void onModelChanged(Match match) throws IOException, ClassNotFoundException {
        notifyMatchChanged(match.createSnapshot(playerIndex));
        newActions(remoteActionsHandler.getRemoteActions(match.getPlayer(), match.getActions()));
    }
    private void setupMatchEvents(){
        match.newActionsEvent.addEventHandler(onNewActionsEventHandler);
    }

    protected abstract void createActionHandler(Player curPlayer) throws RemoteException;
    protected abstract void notifyMatchChanged(MatchSnapshot matchSnapshot) throws IOException;
    protected abstract void newActions(List<RemoteAction> actions) throws IOException, ClassNotFoundException;
    protected abstract void sendMessage(String message) throws IOException;

    private void removeEvents() {
        if(joinedRoom != null) {
            joinedRoom.timerStartEvent.removeEventHandler(timerStartEventHandler);
            joinedRoom.timerTickEvent.removeEventHandler(timerTickEventHandler);
            joinedRoom.timerStopEvent.removeEventHandler(timerStopEventHandler);
            joinedRoom.newPlayerEvent.removeEventHandler(newPlayerEventHandler);
            joinedRoom.playerDisconnectedEvent.removeEventHandler(playerDisconnectedEventHandler);
            joinedRoom.matchStartedEvent.removeEventHandler(matchStartedEventHandler);
            if(match!= null)
                match.newActionsEvent.removeEventHandler(onNewActionsEventHandler);
        }
    }

    private void setupRoomEvents()
    {
        joinedRoom.timerStartEvent.addEventHandler(timerStartEventHandler);
        joinedRoom.timerTickEvent.addEventHandler(timerTickEventHandler);
        joinedRoom.timerStopEvent.addEventHandler(timerStopEventHandler);
        joinedRoom.newPlayerEvent.addEventHandler(newPlayerEventHandler);
        joinedRoom.playerDisconnectedEvent.addEventHandler(playerDisconnectedEventHandler);
        joinedRoom.matchStartedEvent.addEventHandler(matchStartedEventHandler);
    }
    private static final String MATCH_STARTING_MESSAGE = "Match is starting";
    private static String newPlayerMessage(String name){ return name + " joined the room";}
    private static String playerDisconnectedMessage(String name){ return name + " left the room";}
    private static String timerStartMessage(int timeout){
        return "Countdown is started: " + timeout + " seconds left\n";
    }
    private static String timerTickMessage(int timeLeft){
        return timeLeft + " seconds left\n";
    }
    private static final String TIMER_STOPPED_MESSAGE = "Countdown stopped\n";
}
