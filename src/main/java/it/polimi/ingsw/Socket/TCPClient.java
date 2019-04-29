package it.polimi.ingsw.Socket;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.InputStreamUtils;
import it.polimi.ingsw.generics.OutputStreamUtils;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

public class TCPClient
{
    public final Event<TCPClient, Object> closedEvent = new Event<>();
    public final InputStreamUtils in;
    public final OutputStreamUtils out;
    private Socket connectedSocket;

    public TCPClient(Socket connectedSocket) throws IOException
    {
        if(!connectedSocket.isConnected())
            throw new NotYetConnectedException();
        this.connectedSocket = connectedSocket;
        this.in = new InputStreamUtils(connectedSocket.getInputStream());
        this.out = new OutputStreamUtils(connectedSocket.getOutputStream());
        in.streamFailEvent.addEventHandler((a,b)-> this.close());
        out.streamFailedEvent.addEventHandler((a,b)->this.close());
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

    public void displayString(String string) {
        //TODO invoke OutputStream methods
    }
}

