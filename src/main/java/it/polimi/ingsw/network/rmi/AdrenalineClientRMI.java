package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.IAdrenalineServer;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class AdrenalineClientRMI extends AdrenalineClient {
    private IAdrenalineServer server;

    public void initialize(IAdrenalineServer server)
    {
        this.server = server;
    }

    @Override
    protected void notifyInterface(int choice) throws RemoteException {
        server.setInterface(choice == 0);
    }

    @Override
    protected List<String> getAvailableColors() throws IOException {
        return server.availableColors();
    }

    @Override
    protected void notifyColor(int colorIndex) throws RemoteException {
        server.setColor(colorIndex);
    }

    @Override
    protected boolean notifyName(String name) throws RemoteException {
        return server.setName(name);
    }

    @Override
    protected void notifyGameLength(int gameLength) throws RemoteException {
        server.setGameLength(gameLength);
    }

    @Override
    protected void notifyGameMap(int choice) throws RemoteException {
        server.setGameMap(choice);
    }

    @Override
    protected void notifyHandleAction(int selection, int extra) throws IOException {
        server.handleAction(selection, extra);
    }

    @Override
    protected void initializeAction(RemoteAction remoteAction) {
        //TODO
    }

    @Override
    public void newMessage(String message) throws RemoteException {
        //todo
    }
}
