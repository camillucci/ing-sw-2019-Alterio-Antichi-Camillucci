package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static it.polimi.ingsw.generics.Utils.tryDo;

public abstract class AdrenalineServer implements IAdrenalineServer
{
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatEvent = new Event<>();
    protected Controller controller = new Controller();
    protected int colorIndex;
    protected String name;
    protected boolean isHost;
    protected Room joinedRoom;
    private Match match;

    @Override
    public List<String> availableColors()
    {
        joinedRoom = controller.getAvailableRoom();
        return joinedRoom.getAvailableColors();
    }

    @Override
    public void setColor(int colorIndex) throws IOException {
        this.colorIndex = colorIndex;
        joinRoom();
    }

    @Override
    public boolean setName(String name) throws RemoteException
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
    public boolean isHost() throws IOException, RemoteException {
        return isHost;
    }

    @Override
    public void ready() throws RemoteException {
        joinedRoom.notifyPlayerReady();
    }

    protected abstract void sendMessage(String message) throws IOException;

    private void setupRoomEvents()
    {
        joinedRoom.timerStartEvent.addEventHandler((a, timeout) -> tryDo( () -> sendMessage(timerStartMessage(timeout))));
        joinedRoom.timerTickEvent.addEventHandler((a, timeLeft) -> tryDo( () -> sendMessage(timerTickMessage(timeLeft))));
        joinedRoom.timerTickEvent.addEventHandler((a, timeLeft) -> tryDo( () -> sendMessage(timerStoppedMessage)));
    }

    private void joinRoom()
    {
        isHost = joinedRoom.addPlayer(colorIndex, name);
        setupRoomEvents();
    }

    private String timerStartMessage(int timeout){
        return "Countdown is started: " + timeout + " seconds left\n";
    }
    private String timerTickMessage(int timeLeft){
        return "" + timeLeft + " seconds left\n";
    }
    private final String timerStoppedMessage = "Countdown stopped\n";
}