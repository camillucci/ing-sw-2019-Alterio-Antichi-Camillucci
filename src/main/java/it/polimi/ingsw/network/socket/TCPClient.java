package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient
{
    public final IEvent<TCPClient, Object> disconnectedEvent = new Event<>();
    private Socket connectedSocket;
    private Logger logger;

    public InputInterface in() throws IOException
    {
        InputStreamUtils ret = new InputStreamUtils(connectedSocket.getInputStream());
        ret.streamFailEvent.addEventHandler((a,b)-> this.close());
        return ret;
    }

    public OutputInterface out() throws IOException
    {
        OutputStreamUtils ret = new OutputStreamUtils(connectedSocket.getOutputStream());
        ret.streamFailedEvent.addEventHandler((a,b)->this.close());
        return ret;
    }

    public TCPClient(Socket connectedSocket)
    {
        if(!connectedSocket.isConnected())
            throw new NotYetConnectedException();
        this.connectedSocket = connectedSocket;
    }

    public static TCPClient connect(String hostname, int port) throws IOException
    {
        return new TCPClient(new Socket(hostname, port));
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

    public void setName(String name) {
        //TODO
    }
}

