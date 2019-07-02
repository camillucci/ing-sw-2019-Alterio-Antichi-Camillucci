package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.CommandQueue;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides the server info and methods that are specific to the RMI type connection. It also inherits the
 * more generic methods by extending AdrenalineServer
 */
public class AdrenalineServerRMI extends AdrenalineServer implements IRMIAdrenalineServer {

    /**
     * Event other classes can subscribe to. When invoked, all subscribers are notified. It is invoked when a new
     * client connects to this server.
     */
    public final IEvent<AdrenalineServerRMI, Object> newClientConnected = new Event<>();
    private ICallbackAdrenalineClient client;
    private Registry registry;
    private CommandQueue commandQueue = new CommandQueue();

    /**
     * Boolean that indicates whether the pinging thread needs to keep running or not
     */
    private boolean stopPinging = false;
    private static final Logger logger = Logger.getLogger("AdrenalineServerRMI");

    /**
     * As long as the stopPinging variable is equal to false, this thread sends periodic pings to the client in order
     * to check if it is still connected.
     */
    private final Thread pingingThread = new Thread(() -> bottleneck.tryDo( () -> {
        while(!getStopPinging()) {
            client.ping();
            Thread.sleep(PING_PERIOD);
        }
    }));

    /**
     * Constructor that assigns the input parameter to their global correspondences
     * @param controller The controller this class is associated with
     */
    public AdrenalineServerRMI(Controller controller) {
        super(controller);
    }

    /**
     * Method used to start a connection with the client (input parameter). It also starts the pinging thread.
     * @param client Client that's going to connect with this server
     */
    public void initialize(ICallbackAdrenalineClient client)
    {
        this.client = client;
        startPinging();
    }

    @Override
    public void registerClient(ICallbackAdrenalineClient client) {
        this.client = client;
        ((Event<AdrenalineServerRMI, Object>)newClientConnected).invoke(this, null);
        startPinging();
    }

    private synchronized boolean getStopPinging() {
        return stopPinging;
    }

    private synchronized void setStopPinging(boolean stopPinging) {
        this.stopPinging = stopPinging;
    }

    /**
     * If the pinging thread is not running already, this method sets the stopPinging variable to false and starts a
     * new pinging thread.
     */
    @Override
    protected void startPinging()
    {
        if(pingingThread.getState() == Thread.State.TERMINATED || pingingThread.getState() == Thread.State.NEW) {
            setStopPinging(false);
            pingingThread.start();
        }
    }

    /**
     * If the pinging thread is running, this method sets the stopPinging variable to true.
     */
    @Override
    protected void stopPinging()
    {
        if(pingingThread.getState() == Thread.State.TERMINATED)
            return;
        setStopPinging(true);
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    protected void sendCommand(Command<View> command) throws IOException {
        commandQueue.run(() -> bottleneck.tryDo(() -> client.newViewCommand(command)));
    }

    /**
     * Public method called by the client in order to check if the connection is functioning
     */
    @Override
    public void ping() {
        // called by client to test connection
    }
}
