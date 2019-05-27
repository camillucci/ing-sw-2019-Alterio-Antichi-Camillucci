package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
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
        view.getLogin().nameEvent.addEventHandler((a, name) -> tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> tryDo(() -> server.out().sendInt(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> tryDo(() -> server.out().sendInt(map)));
        view.getLogin().rmiEvent.addEventHandler((a, choice) -> tryDo(() -> server.out().sendBool(choice)));
        view.getLogin().socketEvent.addEventHandler((a, choice) -> tryDo(() -> server.out().sendBool(choice)));
    }

    @Override
    protected void connect() throws IOException, NotBoundException {
        this.server = TCPClient.connect(serverName, serverPort);
    }

    @Override
    protected void notifyColor(int colorIndex) throws IOException, ClassNotFoundException {
        server.out().sendInt(colorIndex);
        if(!server.in().getBool())
            view.getLogin().notifyAvailableColor(server.in().getObject());
        else {
            boolean isHost = server.in().getBool();
            if (isHost) {
                view.getLogin().gameMapEvent.addEventHandler((a, b) -> tryDo(this::waitForMessage));
                view.getLogin().notifyHost(true);
            } else {
                view.getLogin().notifyHost(false);
                waitForMessage();
            }
        }
    }

    private void waitForMessage() throws IOException, ClassNotFoundException {
        String message = server.in().getObject();
        while(!message.equals(AdrenalineServerSocket.MATCH_STARTED_MESSAGE)) {
            newMessage(message);
            message = server.in().getObject();
        }
        MatchSnapshot matchSnapshot = server.in().getObject();
        matchStart(matchSnapshot);
    }

    @Override
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        server.out().sendObject(name);
        boolean accepted = server.in().getBool();
        view.getLogin().notifyAccepted(accepted);
        if(accepted)
            view.getLogin().notifyAvailableColor(server.in().getObject());
    }

    private void manageActions(List<RemoteActionSocket> options) throws IOException, ClassNotFoundException {
        view.getActionHandler().choiceEvent.addEventHandler((a, choice) -> tryDo( () -> options.get(choice).initialize(server))); //communicates choice to server
        view.getActionHandler().chooseAction(new ArrayList<>(options));
    }

    protected void waitForAction() throws IOException, ClassNotFoundException {
            manageActions(server.in().getObject());
    }
}
