package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineClientRMI extends AdrenalineClient implements ICallbackAdrenalineClient
{
    /**
     * RMI of the server
     */
    private IRMIAdrenalineServer server;
    /**
     * True if client does not ping the client
     */
    private boolean stopPinging = true;
    /**
     * Hostname of the server
     */
    private String serverName;
    /**
     * Server port
     */
    private int serverPort;
    private final Logger logger = Logger.getLogger("AdrenalineClientRMI");

    /**
     * Thread that pings the server with period of PING_PERIOD
     */
    private final Thread pingingThread = new Thread(() -> bottleneck.tryDo( () -> {
        while(!getStopPinging()) {
            server.ping();
            Thread.sleep(PING_PERIOD);
        }
    }));

    /**
     * Constructor that assigns the two input variables to their global correspondences
     * @param serverName String that represents the name of the server
     * @param serverPort Integer that represents the port of the server
     * @param view Reference to the view
     */
    public AdrenalineClientRMI(String serverName, int serverPort, View view) {
        super(view);
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    /**
     * Methods that belongs to RMI of the client. It's called by server to test connection.
     */
    @Override
    public void ping() {
        // called by server periodically to test connection
    }

    /**
     * Gets RMI of the server, exports and register to server this class (as ICallBackAdrenalineClient) so that can be invoked remotely
     */
    @Override
    protected void connect() throws IOException, NotBoundException {
        UnicastRemoteObject.exportObject(this, 0);
        server = (IRMIAdrenalineServer) LocateRegistry.getRegistry(serverName, this.serverPort).lookup("Server");
        server.registerClient(this);
    }

    /**
     * If the pingingThread isn't already running, this method sets the stopPinging variable to false and starts
     * the pinging thread.
     */
    @Override
    protected void startPing() {
        if(pingingThread.getState() == Thread.State.TERMINATED || pingingThread.getState() == Thread.State.NEW) {
            setStopPinging(false);
            pingingThread.start();
        }
    }

    @Override
    protected void sendServerCommand(Command<IAdrenalineServer> command) throws IOException
    {
        server.newServerCommand(command);
    }

    /**
     * After having checked that the pinging thread is running, this method sets the stopPinging variable to true,
     * causing the thread to stop.
     */
    @Override
    protected void stopPing()
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
    private synchronized void setStopPinging(boolean value){
        stopPinging = value;
    }
    private synchronized boolean getStopPinging(){
        return stopPinging;
    }
}
