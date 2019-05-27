package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


public abstract class AdrenalineServer implements IAdrenalineServer
{
    private final Controller controller;
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatEvent = new Event<>();
    protected String name;
    protected boolean isHost;
    private Room joinedRoom;
    private List<String> availableColors;
    private List<String> otherPlayers = new ArrayList<>();
    private Bottleneck bottleneck = new Bottleneck();
    private BiConsumer<Room, Integer> timerStartEventHandler = (a, timeout) -> bottleneck.tryDo( () -> sendMessage(timerStartMessage(timeout)));
    private BiConsumer<Room, Integer> timerTickEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(timerTickMessage(timeLeft)));
    private BiConsumer<Room, Integer> timerStopEventHandler = (a, timeLeft) -> bottleneck.tryDo( () -> sendMessage(TIMER_STOPPED_MESSAGE));
    private BiConsumer<Room, String> newPlayerEventHandler = (a, name) -> notifyPlayer(name);
    private BiConsumer<Room, String> playerDisconnectedEventHandler = (a, name) -> notifyPlayerDisconnected(name);
    private BiConsumer<Room, Match> matchStartedEventHandler = (a, match) -> bottleneck.tryDo( () -> onMatchStarted(match));

    public AdrenalineServer(Controller controller){
        this.controller = controller;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
    }

    protected void onExceptionGenerated(Exception e){
        e.printStackTrace();
        if(joinedRoom != null)
            removeEvents();
        controller.notifyPlayerDisconnected(name);
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
    public boolean setName(String name)
    {
        if(controller.newPlayer(name)) {
            this.name = name;
            return true;
        }
        return false;
    }

    @Override
    public void setGameLength(int gameLength) {
        joinedRoom.setGameLength(gameLength);
    }

    @Override
    public void setGameMap(int choice) {
        joinedRoom.setGameSize(choice);
    }

    @Override
    public boolean isHost() {
        return joinedRoom.isHost(name);
    }

    private synchronized void notifyPlayer(String name){
        if(otherPlayers.contains(name))
            return;
        otherPlayers.add(name);
        bottleneck.tryDo(() -> sendMessage(newPlayerMessage(name)));
    }

    private synchronized void notifyPlayerDisconnected(String name){
        if(otherPlayers.contains(name))
            otherPlayers.remove(name);
        otherPlayers.add(name);
        bottleneck.tryDo(() -> sendMessage(playerDisconnectedMessage(name)));
    }
    @Override
    public void ready() {
        joinedRoom.notifyPlayerReady(name);
        setupRoomEvents();
        for(String name : joinedRoom.getPlayerNames())
            notifyPlayer(name);
    }

    protected abstract void sendMessage(String message) throws IOException;
    protected abstract void notifyMatchStarted(MatchSnapshot matchSnapshot) throws IOException;

    private void removeEvents() {
        joinedRoom.timerStartEvent.removeEventHandler(timerStartEventHandler);
        joinedRoom.timerTickEvent.removeEventHandler(timerTickEventHandler);
        joinedRoom.timerStopEvent.removeEventHandler(timerStopEventHandler);
        joinedRoom.newPlayerEvent.removeEventHandler(newPlayerEventHandler);
        joinedRoom.playerDisconnectedEvent.removeEventHandler(playerDisconnectedEventHandler);
        joinedRoom.matchStartedEvent.removeEventHandler(matchStartedEventHandler);
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

    private void onMatchStarted(Match match) throws IOException {
        for(Player p : match.getPlayers())
            if(p.name.equals(name)) {
                notifyMatchStarted(new MatchSnapshot(match, p));
                return;
            }
    }
    private String newPlayerMessage(String name){ return name + " joined the room";}
    private String playerDisconnectedMessage(String name){ return name + " left the room";}
    private String timerStartMessage(int timeout){
        return "Countdown is started: " + timeout + " seconds left\n";
    }
    private String timerTickMessage(int timeLeft){
        return timeLeft + " seconds left\n";
    }
    private static final String TIMER_STOPPED_MESSAGE = "Countdown stopped\n";
}
