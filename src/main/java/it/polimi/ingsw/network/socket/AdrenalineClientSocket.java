package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;

/**
 * This class implements a Socket based connection with the server and is used only if the user chooses so.
 * It implements through socket logic all the abstract methods in AdrenalineClient.
 */
public class AdrenalineClientSocket extends AdrenalineClient {
    /**
     * Reference to the instance of server this class is communicating with.
     */
    private TCPClient server;

    /**
     * Sets global parameters and signs up to bottleNeck event, which notifies when a disconnection occurs.
     * @param serverName
     * @param serverPort Port of the server this class communicates with
     * @param view Reference to the interface which communicates with the user
     */
    public AdrenalineClientSocket(String serverName, int serverPort, View view)
    {
        super(serverName, serverPort, view);
    }

    /**
     * Signs up to name, color game length, map type and new command events. Once an event triggers, this class
     * communicates user's choice to server. Also signs up to actionDone event.
     */
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

    /**
     * connects to server through Socket logic.
     * @throws IOException
     */
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
