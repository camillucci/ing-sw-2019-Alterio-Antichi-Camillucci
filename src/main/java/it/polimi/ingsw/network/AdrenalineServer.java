package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.generics.Utils.tryDo;

public abstract class AdrenalineServer implements IAdrenalineServer
{
    private final Controller controller;
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatEvent = new Event<>();
    protected int colorIndex;
    protected String name;
    protected boolean isHost;
    protected Room joinedRoom;

    public AdrenalineServer(Controller controller){
        this.controller = controller;
    }

    @Override
    public List<String> availableColors()
    {
        joinedRoom = controller.getAvailableRoom();
        return joinedRoom.getAvailableColors();
    }

    @Override
    public void setColor(int colorIndex) {
        this.colorIndex = colorIndex;
        isHost = joinedRoom.addPlayer(colorIndex, name);
        setupRoomEvents();
    }

    @Override
    public boolean setName(String name)
    {
        if(!controller.existName(name)) {
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
        return isHost;
    }

    @Override
    public void ready() {
        joinedRoom.notifyPlayerReady();
    }

    protected abstract void sendMessage(String message) throws IOException;
    protected abstract void notifyMatchStarted(MatchSnapshot matchSnapshot) throws IOException;

    private void setupRoomEvents()
    {
        joinedRoom.timerStartEvent.addEventHandler((a, timeout) -> tryDo( () -> sendMessage(timerStartMessage(timeout))));
        joinedRoom.timerTickEvent.addEventHandler((a, timeLeft) -> tryDo( () -> sendMessage(timerTickMessage(timeLeft))));
        joinedRoom.timerStopEvent.addEventHandler((a, timeLeft) -> tryDo( () -> sendMessage(TIMER_STOPPED_MESSAGE)));
        joinedRoom.matchStartedEvent.addEventHandler((a, match) -> tryDo( () -> onMatchStarted(match) ));
    }

    private void onMatchStarted(Match match) throws IOException {
        for(Player p : match.getPlayers())
            if(p.name.equals(name)) {
                notifyMatchStarted(new MatchSnapshot(match, p));
                return;
            }
    }
    private String timerStartMessage(int timeout){
        return "Countdown is started: " + timeout + " seconds left\n";
    }
    private String timerTickMessage(int timeLeft){
        return "" + timeLeft + " seconds left\n";
    }
    private static final String TIMER_STOPPED_MESSAGE = "Countdown stopped\n";
}
