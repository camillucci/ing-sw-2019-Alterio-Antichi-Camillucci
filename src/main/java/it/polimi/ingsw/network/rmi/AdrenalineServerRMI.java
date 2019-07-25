package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.CommandQueue;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 * This class provides the server info and methods that are specific to the RMI type connection. It also inherits the
 * more generic methods by extending AdrenalineServer
 */
public class AdrenalineServerRMI extends AdrenalineServer implements IRMIAdrenalineServer {

    /**
     * Event other classes can subscribe to. When invoked, all subscribers are notified. It is invoked when a new
     * client connects to this server.
     */
    public final IEvent<AdrenalineServerRMI, Object> newClientConnectedEvent = new Event<>();
    public final IEvent<AdrenalineServerRMI, Object> clientDisconnectedEvent = new Event<>();
    private ICallbackAdrenalineClient client;
    private CommandQueue commandQueue = new CommandQueue();

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
    }

    @Override
    public void registerClient(ICallbackAdrenalineClient client) {
        this.client = client;
        ((Event<AdrenalineServerRMI, Object>) newClientConnectedEvent).invoke(this, null);
    }

    @Override
    protected void sendCommand(Command<View> command) {
        commandQueue.run(() -> bottleneck.tryDo(() -> client.newViewCommand(command)));
    }

    /**
     * Public method called by the client in order to check if the connection is functioning
     */
    @Override
    public void ping() {
        // called by client to test connection
    }

    public void pingClient() throws RemoteException {
        try {
            client.ping();
        } catch (RemoteException e) {
            onExceptionGenerated();
            throw e;
        }
    }

    @Override
    public void close(){
        try {
            UnicastRemoteObject.unexportObject(this, true);
            ((Event<AdrenalineServerRMI, Object>)clientDisconnectedEvent).invoke(this, null);
            logger.log(Level.INFO, () -> name + " disconnected by server");
            isDisconnected = true;
        } catch (NoSuchObjectException e) {
            // nothing
        }
    }
}
