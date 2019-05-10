package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPListener {
    public final IEvent<TCPListener, TCPClient> newClientEvent = new Event<>();
    public final IEvent<TCPListener, TCPClient> clientDisconnectedEvent = new Event<>();
    private ServerSocket listener;
    private int port;
    private int maxConnected;
    private Thread listenThread;
    private Logger logger;

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

        if (listenThread == null || listenThread.getState() == Thread.State.TERMINATED)
            return;
        try {
            listener.close();
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "IOException, Class TCPListener, Line 51", e);
        }
        try {
            listenThread.join();
        }
        catch(InterruptedException e) {
            logger.log(Level.WARNING, "InterruptedException, Class TCPListener, Line 55", e);
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
        catch (IOException e) {
            //logger.log(Level.WARNING, "IOException, Class TCPListener, Line 77", e);
        }
        finally { closeListener(); }
    }

    private void closeListener()
    {
        try
        {
            listener.close();
        }
        catch(IOException e)
        {
            logger.log(Level.WARNING, "IOException, Class TCPListener, Line 90", e);
        }
    }
    private synchronized void onDisconnection (TCPClient client)
    {
        connectedHosts.remove(client);
        ((Event<TCPListener,TCPClient>)clientDisconnectedEvent).invoke(this, client);
    }
    private void addConnected(TCPClient connectedHost)
    {
        connectedHosts.add(connectedHost);

        // if newClientEvents is invoked  in this thread and calls this.stop() the thread joins itself -> deadlock
        // for this reason a tmp thread invokes the event
        (new Thread(()-> ((Event<TCPListener,TCPClient>)this.newClientEvent).invoke(this, connectedHost))).start();
    }

    private List<TCPClient> connectedHosts = new ArrayList<>();
}
