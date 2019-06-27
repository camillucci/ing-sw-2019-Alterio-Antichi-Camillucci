package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides the server info and methods that are specific to the Socket type connection. It also inherits the
 * more generic methods by extending AdrenalineServer
 */
public class AdrenalineServerSocket extends AdrenalineServer
{
    /**
     * reference to the client connected to the server
     */
    private TCPClient client;

    /**
     * Constructor that assigns the input parameters to their global correspondences and starts the pinging thread that
     * periodically confirms that the connection with the client is still functioning
     * @param client reference to the client connected to the server
     * @param controller reference to the controller this server communicates with
     */
    public AdrenalineServerSocket(TCPClient client, Controller controller)
    {
        super(controller);
        this.client = client;
        startPinging();
    }

    public void start() {
        bottleneck.tryDo(this::waitForCommand);
    }
    private void waitForCommand() throws IOException, ClassNotFoundException {
        while(true)
            newServerCommand(client.in().getObject());
    }

    /**
     * Starts the pinging thread that pings the client periodically in order to confirm that the connection is still
     * functioning
     */
    @Override
    protected void startPinging() {
        client.startPinging(PING_PERIOD, this::onExceptionGenerated);
    }

    /**
     * Stops the pinging thread.
     */
    @Override
    protected void stopPinging() {
        client.stopPinging();
    }

    @Override
    protected void sendCommand(Command<View> command) throws IOException {
        client.out().sendObject(command);
    }
}
