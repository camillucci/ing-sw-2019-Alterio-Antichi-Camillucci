package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.generics.Utils.tryDo;

public class AdrenalineClientSocket extends AdrenalineClient {
    private TCPClient server;

    public AdrenalineClientSocket(String serverName, int serverPort, View view) throws IOException
    {
        super(serverName, serverPort, view);
    }

    @Override
    protected void setupView()
    {
        view.login.nameEvent.addEventHandler((a,name) -> tryDo( () -> notifyName(name)));
        view.login.colorEvent.addEventHandler((a, color) -> tryDo(() -> notifyColor(color)));
        view.login.gameLengthEvent.addEventHandler((a,len) -> tryDo(() -> server.out().sendInt(len)));
        view.login.gameMapEvent.addEventHandler((a,map) -> tryDo(() -> server.out().sendInt(map)));
    }

    @Override
    protected void connect() throws IOException, NotBoundException {
        this.server = TCPClient.connect(serverName, serverPort);
    }

    @Override
    protected void notifyColor(int colorIndex) throws IOException {
        server.out().sendInt(colorIndex);
        view.login.notifyHost(server.in().getBool());
    }

    @Override
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        server.out().sendObject(name);
        boolean accepted = server.in().getBool();
        view.login.notifyAccepted(accepted);
        if(accepted)
            view.login.notifyAvailableColor(server.in().getObject());
    }

    @Override
    public void modelChanged(MatchSnapshot matchSnapshot) throws RemoteException {
        //TODO
    }
}
