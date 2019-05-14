package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.IAdrenalineServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public class AdrenalineClientRMI extends AdrenalineClient {
    private IAdrenalineServer server;

    public AdrenalineClientRMI(String serverName, int port) throws RemoteException, NotBoundException
    {
        this.server = RMIClient.connect(serverName, port);
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
}
