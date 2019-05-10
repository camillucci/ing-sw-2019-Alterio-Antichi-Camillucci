package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.rmi.AdrenalineServerRMI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public abstract class AdrenalineServer extends ConnectionAbstract implements IAdrenalineServer
{
    public final IEvent<AdrenalineServer, List<String>> matchStartEvent = new Event<>();
    public final IEvent<AdrenalineServer, MatchSnapshot> viewUpdatedEvent = new Event<>();
    boolean connected = false;
    protected Controller controller;
    protected boolean gameInterface;
    protected int colorIndex;
    protected int gameLength;
    protected int gameMap;
    protected String name;
    protected RemoteActionsHandler remoteActionsHandler;

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
       // return controller.getAvailableRoom().getAvailableColors();
        return null;
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
            return true;
        }
        return false;
    }

    @Override
    public void connect()
    {
        if(!connected) {
            ((Event<ConnectionAbstract, Object>)this.connectedEvent).invoke(this, null);
            connected = true;
        }
    }

    @Override
    public void setGameLength(int gameLength) throws RemoteException {
        this.gameLength = gameLength;
    }

    @Override
    public void setGameMap(int choice) throws RemoteException {
        this.gameMap = choice;
    }

    @Override
    public boolean joinRoom() throws IOException
    {
        return controller.getAvailableRoom().addPlayer(colorIndex, name, this);
    }

    @Override
    public void matchStart() throws IOException {
        ((Event<AdrenalineServer, List<String>>) matchStartEvent).invoke(this, controller.getPlayerNames());
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
}