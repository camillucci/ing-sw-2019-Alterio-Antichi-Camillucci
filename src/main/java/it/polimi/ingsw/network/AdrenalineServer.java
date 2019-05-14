package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class AdrenalineServer extends ConnectionAbstract implements IAdrenalineServer
{
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatedEvent = new Event<>();
    public final IEvent<IAdrenalineServer, String> newMessageEvent = new Event<>();
    boolean connected = false;
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
    public List<String> availableColors() throws RemoteException
    {
      return controller.getAvailableRoom().getAvailableColors();
    }

    @Override
    public void setColor(int colorIndex) throws RemoteException {
        this.colorIndex = colorIndex;
    }

    @Override
    public boolean setName(String name) throws RemoteException
    {
        if(controller.existName(name)) {
            this.name = name;
            ((Event<IAdrenalineServer, String>)newMessageEvent).invoke(this, "Name ok");
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

    @Override
    public void updateView(MatchSnapshot matchSnapshot) throws IOException {
        ((Event<AdrenalineServer, MatchSnapshot>) viewUpdatedEvent).invoke(this, matchSnapshot);
    }

    @Override
    public void handleAction(int selection, int extra) {
        //remoteActionsHandler(selection, extra);
        //TODO
    }

    @Override
    public void subscribeNewMessageEvent(BiConsumer<IAdrenalineServer, String> eventHandler)
    {
        this.newMessageEvent.addEventHandler(eventHandler);
    }
}