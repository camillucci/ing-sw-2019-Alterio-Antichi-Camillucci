package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.generics.*;
import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

public class TCPClient extends Client
{
    public final Event<TCPClient, Object> closedEvent = new Event<>();
    private Socket connectedSocket;

    @Override
    public InputInterface in() throws IOException
    {
        InputStreamUtils ret = new InputStreamUtils(connectedSocket.getInputStream());
        ret.streamFailEvent.addEventHandler((a,b)-> this.close());
        return ret;
    }

    @Override
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
        try{
            connectedSocket.close();
            closedEvent.invoke(this, null);
        }
        catch(IOException e) {/*TODO*/}
    }

    public void setName(String name) {

    }
}

