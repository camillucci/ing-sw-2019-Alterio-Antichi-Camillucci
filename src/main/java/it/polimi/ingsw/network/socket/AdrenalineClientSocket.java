package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdrenalineClientSocket extends AdrenalineClient {
    private TCPClient server;

    public AdrenalineClientSocket(String serverName, int port) throws IOException
    {
        this.server = TCPClient.connect(serverName, port);
    }

    @Override
    protected void notifyInterface(int choice) throws IOException {
        server.out().sendBool(choice == 0);
    }

    @Override
    protected List<String> getAvailableColors() throws IOException, ClassNotFoundException {
        return server.in().getObject();
    }

    @Override
    protected void notifyColor(int colorIndex) throws IOException {
        server.out().sendInt(colorIndex);
    }

    @Override
    protected boolean notifyName(String name) throws IOException {
        server.out().sendObject(name);
        return server.in().getBool();
    }

    @Override
    protected void notifyGameLength(int gameLength) throws IOException {
        server.out().sendInt(gameLength);
    }

    @Override
    protected void notifyGameMap(int choice) throws IOException {
        server.out().sendInt(choice);
    }

    @Override
    protected void notifyHandleAction(int selection, int extra) {
        //TODO
    }

    @Override
    protected void inizialize(RemoteAction remoteAction) {
        //TODO
    }
}
