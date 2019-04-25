package it.polimi.ingsw.Socket;

import it.polimi.ingsw.generics.Event;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class TCPListener
{
    public Event<TCPListener, TCPClient> newClientEvent = new Event<>();
    public Event<TCPListener, TCPClient> clientDisconnectedEvent = new Event<>();
    private ServerSocket listener;
    private int port;
    private int maxConnected;
    private Thread listenThread = null;

    public TCPListener(int port, int maxConnected)
    {
        this.maxConnected = maxConnected;
        this.port = port;
    }

    public void start()
    {
        if(isListening())
            return;
        (new Thread(this::listenThread)).start();
    }

    public boolean isListening()
    {
        return getListenThread() != null;
    }

    public void stop()
    {
        try
        {
            if(getListenThread() == null)
                return;

            listener.close();

            if(Thread.currentThread() != getListenThread())
                listenThread.join();
            setNullListenThread();
        }
        catch(IOException ecc)
        {
            listener = null;
        }
        catch(InterruptedException ecc)
        {
            Thread.currentThread().interrupt();
            stop();
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
            if(connectedHosts.size() >= maxConnected)
                return;
            listener = new ServerSocket(port);
            do
            {
                this.listenThread = Thread.currentThread();
                TCPClient tmp = new TCPClient(listener.accept());
                tmp.closingEvent.addEventHandler((closingClient, b) -> onDisconnection((closingClient)));
                addConnected(tmp);
            } while(connectedHosts.size() < maxConnected);
        }
        catch (Exception ecc){}
        finally { stop(); }
    }

    private void onDisconnection (TCPClient client)
    {
        removeConnected(client);
        clientDisconnectedEvent.invoke(this, client);
    }
    private synchronized void addConnected(TCPClient connectedHost)
    {
        connectedHosts.add(connectedHost);
        this.newClientEvent.invoke(this, connectedHost);
    }
    private synchronized void removeConnected(TCPClient connectedHost)
    {
        connectedHosts.remove(connectedHost);
    }

    private synchronized Thread getListenThread()
    {
        return listenThread;
    }

    private synchronized void setNullListenThread()
    {
        listenThread = null;
    }

    private List<TCPClient> connectedHosts = new ArrayList<>();
    private Object listLock = new Object();
}
