package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
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
        super.setupView();
        view.getActionHandler().actionDoneEvent.addEventHandler((a, choice) -> bottleneck.tryDo(this::waitForCommand)); //communicates choice to server
    }

    @Override
    protected void notifyName(String name) throws IOException, ClassNotFoundException {
        super.notifyName(name);
        waitForCommand();
    }

    @Override
    protected void connect() throws IOException {
        this.server = TCPClient.connect(serverName, serverPort);
    }

    @Override
    protected void sendServerCommand(Command<IAdrenalineServer> command) throws IOException {
        server.out().sendObject(command);
    }

    @Override
    protected void startPing() {
       server.startPinging(PING_PERIOD, this::onExceptionGenerated);
    }

    private void waitForCommand() throws IOException, ClassNotFoundException {
        while(true)
            newViewCommand(server.in().getObject());
    }

    @Override
    protected void stopPing() {
        server.stopPinging();
    }
}
