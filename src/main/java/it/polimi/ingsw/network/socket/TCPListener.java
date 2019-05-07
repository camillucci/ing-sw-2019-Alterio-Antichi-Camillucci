package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.Event;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class TCPListener {
    public final Event<TCPListener, TCPClient> newClientEvent = new Event<>();
    public final Event<TCPListener, TCPClient> clientDisconnectedEvent = new Event<>();
    private ServerSocket listener;
    private int port;
    private int maxConnected;
    private Thread listenThread;

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
        try
        {
            if (listenThread == null || listenThread.getState() == Thread.State.TERMINATED)
                return;

            listener.close();
            listenThread.join();
         }
        catch (IOException e) {/*TODO best practice?*/}
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            //stop(); //TODO best practice?
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
            //TODO
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
            // TODO best practice?
        }
    }
    private synchronized void onDisconnection (TCPClient client)
    {
        connectedHosts.remove(client);
        clientDisconnectedEvent.invoke(this, client);
    }
    private void addConnected(TCPClient connectedHost)
    {
        connectedHosts.add(connectedHost);

        // if newClientEvents is invoked  in this thread and calls this.stop() the thread joins itself -> deadlock
        // for this reason a tmp thread invokes the event
        (new Thread(()->this.newClientEvent.invoke(this, connectedHost))).start();
    }

    private List<TCPClient> connectedHosts = new ArrayList<>();
}
