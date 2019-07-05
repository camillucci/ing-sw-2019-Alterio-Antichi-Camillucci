package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.CommandQueue;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used as a managing class for all the TCPClients instances that are located server side. Each of those
 * TCPClient instances corresponds to an actual client they're connected to.
 */
public class TCPListener {

    /**
     * Event other classes can subscribe to. When this event is invoked, every subscriber is notified. This event is
     * invoked when a new client connects to this server.
     */
    public final IEvent<TCPListener, TCPClient> newClientEvent = new Event<>();
    private CommandQueue eventQueue = new CommandQueue();
    /**
     * Event other classes can subscribe to. When this event is invoked, every subscriber is notified. This event is
     * invoked when a client disconnects from this server.
     */
    public final IEvent<TCPListener, TCPClient> clientDisconnectedEvent = new Event<>();

    /**
     * Server this class is associated with
     */
    private ServerSocket listener;

    /**
     * Port that characterizes the server
     */
    private int port;
    private Thread pingingThread;
    /**
     * Integer that represents the maximum amount of connected clients the server can support all at once
     */
    private int maxConnected;
    private Thread listenThread;
    private static final Logger logger = Logger.getLogger("TCPListener");
    private boolean stopPinging;

    public TCPListener(int port)
    {
        this(port, Integer.MAX_VALUE);
    }

    public TCPListener(int port, int maxConnected) {
        this.maxConnected = maxConnected;
        this.port = port;
    }
    public synchronized void start() throws IOException {
        if ((listenThread != null && listenThread.getState() != Thread.State.TERMINATED) || connectedHosts.size() >= maxConnected)
            return;
        listenThread = new Thread(this::listenThread);
        listener = new ServerSocket(port);
        listenThread.start();
    }

    public synchronized boolean isListening() {
        return listener != null && listenThread.getState()!=Thread.State.TERMINATED;
    }

    public synchronized void stop() {

        if ( listener == null || listenThread == null || listenThread.getState() == Thread.State.TERMINATED)
            return;
        try
        {
            listener.close();
            listenThread.join();
        }
        catch(InterruptedException | IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public synchronized List<TCPClient> getConnected()
    {
        return new ArrayList<>(connectedHosts);
    }

    private void listenThread()
    {
        try
        {
            do
            {
                TCPClient tmp = new TCPClient(listener.accept());
                tmp.disconnectedEvent.addEventHandler((closingClient, b) -> onDisconnection(((TCPClient)closingClient)));
                addConnected(tmp);
            } while(connectedHosts.size() < maxConnected);
        }
        catch (IOException ignored) { }
        finally { closeListener(); }
    }

    public synchronized void pingAll(int period){
        if(pingingThread != null && pingingThread.getState() != Thread.State.TERMINATED)
            return;

        pingingThread = new Thread(() -> {
            try {
                while (!getStopPinging()) {
                    for(TCPClient client : getConnected())
                        client.out().ping();
                    Thread.sleep(period);
                }
            } catch (IOException | InterruptedException e) {
                stopPinging = true;
            }
        });
        pingingThread.start();
    }

    public synchronized void stopPinging(){
        if(pingingThread == null || pingingThread.getState() == Thread.State.TERMINATED)
            return;

        setStopPinging(true);
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
    private synchronized void setStopPinging(boolean stopPinging){
        this.stopPinging = stopPinging;
    }

    private synchronized boolean getStopPinging(){
        return stopPinging;
    }

    private void closeListener()
    {
        try
        {
            listener.close();
        }
        catch(IOException e)
        {
            logger.log(Level.WARNING, "IOException, Class TCPListener", e);
        }
    }

    /**
     * Method called when the connection with a client is interrupted.
     * @param client Reference to the disconnected client
     */
    private synchronized void onDisconnection (TCPClient client)
    {
        connectedHosts.remove(client);
        ((Event<TCPListener,TCPClient>)clientDisconnectedEvent).invoke(this, client);
    }
    private synchronized void addConnected(TCPClient connectedHost)
    {
        connectedHosts.add(connectedHost);

        // if newClientEvent is invoked in this thread and calls this.stop() the thread joins itself -> deadlock
        // for this reason a tmp thread invokes the event
        eventQueue.run(()-> ((Event<TCPListener,TCPClient>)this.newClientEvent).invoke(this, connectedHost));
    }

    private List<TCPClient> connectedHosts = new ArrayList<>();
}
