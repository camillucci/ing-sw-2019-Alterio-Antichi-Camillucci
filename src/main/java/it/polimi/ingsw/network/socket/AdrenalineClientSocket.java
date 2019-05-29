package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;

public class AdrenalineClientSocket extends AdrenalineClient {
    private TCPClient server;

    public AdrenalineClientSocket(String serverName, int serverPort, View view)
    {
        super(serverName, serverPort, view);
    }

    @Override
    protected void setupView()
    {
        view.getLogin().nameEvent.addEventHandler((a, name) -> bottleneck.tryDo( () -> notifyName(name)));
        view.getLogin().colorEvent.addEventHandler((a, color) -> bottleneck.tryDo(() -> notifyColor(color)));
        view.getLogin().gameLengthEvent.addEventHandler((a, len) -> bottleneck.tryDo(() -> server.out().sendInt(len)));
        view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> server.out().sendInt(map)));
        view.getLogin().rmiEvent.addEventHandler((a, choice) -> bottleneck.tryDo(() -> server.out().sendBool(choice)));
        view.getLogin().socketEvent.addEventHandler((a, choice) -> bottleneck.tryDo(() -> server.out().sendBool(choice)));
        view.getActionHandler().choiceEvent.addEventHandler((a, choice) -> bottleneck.tryDo( () -> ((RemoteActionSocket)choice).initialize(server))); //communicates choice to server
        view.getActionHandler().actionDoneEvent.addEventHandler((a, choice) -> bottleneck.tryDo(this::waitForActions)); //communicates choice to server
    }

    @Override
    protected void startPing() {
       server.startPinging(PING_PERIOD, this::onExceptionGenerated);
    }

    @Override
    public void newActions(List<RemoteAction> newActions) {
        bottleneck.tryDo( () -> manageActions(newActions));
    }

    @Override
    protected void connect() throws IOException {
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
                view.getLogin().gameMapEvent.addEventHandler((a, b) -> bottleneck.tryDo(this::waitForMatchStart));
                view.getLogin().notifyHost(true);
            } else {
                view.getLogin().notifyHost(false);
                waitForMatchStart();
            }
        }
    }

    private void waitForMatchStart() throws IOException, ClassNotFoundException {
        do{
            newMessage(server.in().getObject());
        }while(!matchStarted);
        view.loginCompleted();
        waitForActions();
    }


    @Override
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        server.out().sendObject(name);
        boolean accepted = server.in().getBool();
        view.getLogin().notifyAccepted(accepted);
        if(accepted)
            view.getLogin().notifyAvailableColor(server.in().getObject());
    }

    @Override
    protected void stopPing() {
        server.stopPinging();
    }


    private void manageActions(List<RemoteAction> options) throws IOException, ClassNotFoundException {
        view.getActionHandler().chooseAction(options);
    }

    private void waitForActions() throws IOException, ClassNotFoundException {
        MatchSnapshot matchSnapshots = server.in().getObject();
        List<RemoteAction> actions = server.in().getObject();
        modelChanged(matchSnapshots);
        newActions(actions);
    }
}
