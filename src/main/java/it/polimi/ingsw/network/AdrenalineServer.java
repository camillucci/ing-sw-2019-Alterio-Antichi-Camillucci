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

public class AdrenalineServer implements IAdrenalineServer
{
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatEvent = new Event<>();
    protected Controller controller;
    protected boolean gameInterface;
    protected int colorIndex;
    protected int gameLength;
    protected int gameMap;
    protected String name;
    private Room joinedRoom;
    private Match match;

    public AdrenalineServer(Controller controller)
    {
        this.controller = controller;
        //this.remoteActionsHandler = remoteActionsHandler;
    }

    @Override
    public void setInterface(boolean CLI) throws RemoteException
    {
        this.gameInterface = CLI;
    }

    @Override
    public List<String> availableColors() throws IOException
    {
        joinRoom();
        return joinedRoom.getAvailableColors();
    }

    @Override
    public void setColor(int colorIndex) throws RemoteException {
        this.colorIndex = colorIndex;
        updateView(null);
    }

    @Override
    public boolean setName(String name) throws RemoteException
    {
        if(controller.existName(name)) {
            this.name = name;
            return true;
        }
        return false;
    }

    @Override
    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    @Override
    public void setGameMap(int choice) {
        this.gameMap = choice;
    }

    @Override
    public boolean joinRoom() throws IOException
    {
        //remove method from interface
        Room room = controller.getAvailableRoom();
        if(room.addPlayer(colorIndex, name, this)) {
            this.joinedRoom = room;
            joinedRoom.matchStartedEvent.addEventHandler((r, match) -> onMatchStart(match));
            return true;
        }
        return false;
    }

    private void onMatchStart(Match match) {
        this.match = match;
        // todo
    }

    protected void updateView(MatchSnapshot matchSnapshot) {
        ((Event<AdrenalineServer, MatchSnapshot>) viewUpdatEvent).invoke(this, matchSnapshot);
    }

    @Override
    public void handleAction(int selection, int extra) {
        //remoteActionsHandler(selection, extra);
        //TODO
    }
}