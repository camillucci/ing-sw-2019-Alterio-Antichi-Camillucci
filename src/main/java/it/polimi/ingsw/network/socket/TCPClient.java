package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to send data via socket by communicating with another instance of the same class. It contains all
 * the info and methods relative to socket connection.
 */
public class TCPClient
{
    /**
     * event other classes can subscribe to. When the event is invoked every subscriber is notified. This event is
     * invoked when the connection with the other TCPClient instance is interrupted.
     */
    public final IEvent<TCPClient, Object> disconnectedEvent = new Event<>();
    private Socket connectedSocket;

    /**
     * This thread is used to periodically send pings to the other TCPClient instance this class is connected to, in
     * order to constantly check that the connection is functioning properly.
     */
    private Thread pingingThread;

    /**
     * Boolean that indicates whether the pingingThread thread needs to stop running. If it assumes the true value the
     * thread stop, otherwise it keeps running.
     */
    private boolean stopPinging = false;

    /**
     * Stream with methods used to receive data from the TCPClient this class is connected to.
     */
    private SocketInputStream in;

    /**
     * Stream with methods used to send data to the TCPClient this class is connected to.
     */
    private SocketOutputStream out;
    private Logger logger = Logger.getLogger("TCPClient");

    public SocketInputStream in()
    {
        return in;
    }

    public SocketOutputStream out()
    {
        return out;
    }

    private synchronized void setStopPinging(boolean stopPinging){
        this.stopPinging = stopPinging;
    }

    private synchronized boolean getStopPinging(){
        return stopPinging;
    }

    /**
     * Constructor that assigns the input parameter to its global correspondence, after having check that the
     * connection is actually established. This method also creates the two streams used to send to and receive data
     * from the TCPClient instance this class is connected with. This method also subscribes to the two streamFail
     * events.
     * @param connectedSocket Reference to the class used for the communication with the other instance of TCPClient
     *                        this class is connected with.
     * @throws IOException IOException
     */
    public TCPClient(Socket connectedSocket) throws IOException {
        if(!connectedSocket.isConnected())
            throw new NotYetConnectedException();

        this.connectedSocket = connectedSocket;
        out = new SocketOutputStream(connectedSocket.getOutputStream());
        out.streamFailedEvent.addEventHandler((a,b)->this.close());
        in = new SocketInputStream(connectedSocket.getInputStream());
        in.streamFailEvent.addEventHandler((a,b)-> this.close());
    }

    /**
     * Establishes a connection with the TCPClient class associated with the hostname and port gotten as input
     * parameters.
     * @param hostname Hostname of the client this class is connecting with
     * @param port port of the client this class is connecting with
     * @return The newly create instance of TCPClient
     * @throws IOException IOException
     */
    public static TCPClient connect(String hostname, int port) throws IOException
    {
        return new TCPClient(new Socket(hostname, port));
    }

    /**
     * This method checks whether there is already a pinging bot running. In case there isn't, this method creates a
     * new pingingThread thread using the period gotten as input and then starts said thread.
     * @param period pinging period
     */
    public void startPinging(int period) {
        if(pingingThread != null && pingingThread.getState() != Thread.State.TERMINATED)
            return;

        pingingThread = new Thread(() -> {
            try {
                while (!getStopPinging()) {
                    out().ping();
                    Thread.sleep(period);
                }
            } catch (IOException e) {
                stopPinging = true;
                pingingThread = null;
            } catch (InterruptedException e) {
                stopPinging = true;
                pingingThread = null;
                Thread.currentThread().interrupt();
            }
        });
        pingingThread.start();
    }

    /**
     * First, it's checked whether there is a pingingThread running already. If that's the case, said thread is stopped
     * by putting stopPinging value equal to true.
     */
    public void stopPinging()
    {
        if(pingingThread == null || pingingThread.getState() == Thread.State.TERMINATED)
            return;

        setStopPinging(true);
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * This method tries to close the socket connection already established and subsequently calls the disconnected
     * event. In case it fail, an exception is thrown.
     */
    public void close()
    {
        try
        {
            connectedSocket.close();
            ((Event<TCPClient,Object>)disconnectedEvent).invoke(this, null);
        }
        catch(IOException e) {
            logger.log(Level.WARNING, "IOException, Class TCPClient", e);
        }
    }
}

