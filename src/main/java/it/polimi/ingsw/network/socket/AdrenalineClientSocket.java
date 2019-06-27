package it.polimi.ingsw.network.socket;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.View;
import java.io.IOException;

/**
 * This class implements a Socket based connection with the server and is used only if the user chooses so.
 * It implements through socket logic all the abstract methods in AdrenalineClient.
 */
public class AdrenalineClientSocket extends AdrenalineClient {
    /**
     * Reference to the instance of server this class is communicating with.
     */
    private TCPClient server;
    private String serverName;
    private int serverPort;

    /**
     * Sets global parameters and signs up to bottleNeck event, which notifies when a disconnection occurs.
     * @param serverName hostname of the server
     * @param serverPort Port of the server this class communicates with
     * @param view Reference to the interface which communicates with the user
     */
    public AdrenalineClientSocket(String serverName, int serverPort, View view)
    {
        super(view);
        this.serverName = serverName;
        this.serverPort = serverPort;
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
    protected void notifyName(String name) {
        super.notifyName(name);
        waitForCommand();
    }

    /**
     * Connects to server through Socket logic.
     */
    @Override
    protected void connect() throws IOException {
        this.server = TCPClient.connect(serverName, serverPort);
    }

    @Override
    protected void sendServerCommand (Command<IAdrenalineServer> command) throws IOException {
        server.out().sendObject(command);
    }

    /**
     * Calls the method on the server that starts a thread dedicated to the periodical pinging of the client. Its role
     * is to make sure that the connection between server and client is always functioning.
     */
    @Override
    protected void startPing() {
       server.startPinging(PING_PERIOD, this::onExceptionGenerated);
    }

    private void waitForCommand()
    {
        bottleneck.tryDo( () ->
        {
            while (true)
                newViewCommand(server.in().getObject());
        });
    }

    @Override
    protected void stopPing() {
        server.stopPinging();
    }
}
