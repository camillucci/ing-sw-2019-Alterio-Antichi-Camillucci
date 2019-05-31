package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient
{
    public final IEvent<TCPClient, Object> disconnectedEvent = new Event<>();
    private Socket connectedSocket;
    private Thread pingingBot;
    private boolean stopPinging = false;
    private InputStreamUtils in;
    private OutputStreamUtils out;
    private Logger logger = Logger.getLogger("TCPClient");

    public InputStreamUtils in() throws IOException
    {
        return in;
    }

    public OutputStreamUtils out() throws IOException
    {
        return out;
    }

    private synchronized void setStopPinging(boolean stopPinging){
        this.stopPinging = stopPinging;
    }

    private boolean getStopPinging(){
        return stopPinging;
    }

    public TCPClient(Socket connectedSocket) throws IOException {
        if(!connectedSocket.isConnected()) {
            throw new NotYetConnectedException();
        }
        this.connectedSocket = connectedSocket;
        connectedSocket.setSoTimeout(2000);
        out = new OutputStreamUtils(connectedSocket.getOutputStream());
        out.streamFailedEvent.addEventHandler((a,b)->this.close());
        in = new InputStreamUtils(connectedSocket.getInputStream());
        in.streamFailEvent.addEventHandler((a,b)-> this.close());
    }

    public static TCPClient connect(String hostname, int port) throws IOException
    {
        return new TCPClient(new Socket(hostname, port));
    }

    public void startPinging(int period, Consumer<Exception> onPingFail) {
        if(pingingBot != null && pingingBot.getState() != Thread.State.TERMINATED)
            return;

        pingingBot = new Thread(() -> {
            try {
                while (!getStopPinging()) {
                    out().ping();
                    Thread.sleep(period);
                }
            } catch (IOException | InterruptedException e) {
                stopPinging = true;
                pingingBot = null;
                onPingFail.accept(e);
            }
        });
        pingingBot.start();
    }

    public void stopPinging()
    {
        if(pingingBot == null || pingingBot.getState() == Thread.State.TERMINATED)
            return;

        setStopPinging(true);
        try {
            pingingBot.join();
        } catch (InterruptedException e) {
            //todo
        }
    }

    public void close()
    {
        try
        {
            connectedSocket.close();
            ((Event<TCPClient,Object>)disconnectedEvent).invoke(this, null);
        }
        catch(IOException e) {
            logger.log(Level.WARNING, "IOException, Class TCPClient, Line 51", e);
        }
    }
}

