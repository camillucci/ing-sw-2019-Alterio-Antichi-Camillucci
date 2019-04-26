package it.polimi.ingsw.Socket;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.StreamIO;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

public class TCPClient
{
    public final Event<TCPClient, Object> closingEvent = new Event<>();
    private Socket connectedSocket;

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
    public void close() throws IOException
    {
        connectedSocket.close();
        closingEvent.invoke(this, null);
    }

    public void sendObject(Serializable object) throws IOException
    {
        send( () -> StreamIO.sendObject(connectedSocket.getOutputStream(), object));
    }
    public void sendMessageAuto(String message) throws IOException
    {
        this.sendBytesAuto(message.getBytes());
    }
    public void sendByte(byte b) throws IOException
    {
        send( () -> connectedSocket.getOutputStream().write(b));
    }
    public void sendByteAuto(byte b) throws IOException
    {
        send( () -> StreamIO.sendByteAuto(connectedSocket.getOutputStream(), b));
    }

    public void sendBytes(byte[] bytes) throws IOException
    {
        send(() -> connectedSocket.getOutputStream().write(bytes));
    }
    public void sendBytesAuto(byte[] bytes) throws IOException
    {
        send( () -> StreamIO.sendBytesAuto(connectedSocket.getOutputStream(), bytes));
    }

    public void sendFile(String filename) throws IOException
    {
       send( () -> StreamIO.sendFile(connectedSocket.getOutputStream(), filename));
    }
    public void sendFileAuto(String filename) throws IOException
    {
        send( () -> StreamIO.sendFileAuto(connectedSocket.getOutputStream(), filename));
    }

    public <T> T getObject() throws IOException, ClassNotFoundException
    {
        return get( () -> StreamIO.getObject(connectedSocket.getInputStream()));
    }
    public int getByte() throws IOException
    {
        return get( () -> connectedSocket.getInputStream().read()); // -1 if End Of Stream is reached
    }

    public byte[] getBytes(long max) throws IOException
    {
        return get ( () -> StreamIO.getBytes(connectedSocket.getInputStream(), max));
    }
    public byte[] getBytesAuto() throws IOException
    {
        return get( ()-> StreamIO.getBytesAuto(connectedSocket.getInputStream()));
    }

    public void getFile(String filename, long fileSize) throws IOException
    {
        get( () -> StreamIO.getFile(connectedSocket.getInputStream(), filename, fileSize));
    }
    public void getFileAuto(String filename) throws IOException
    {
        get( ()-> StreamIO.getFileAuto(connectedSocket.getInputStream(), filename));
    }
    public String getMessageAuto() throws IOException
    {
        return new String(getBytesAuto());
    }

    private void send(streamActionInterface sendFunc) throws IOException
    {
        try
        {
            sendFunc.invoke();
        }
        catch(IOException ecc)
        {
            close();
            throw ecc;
        }
    }

    private <T, E extends Exception> T get(getInterface<T, E> getFunc) throws IOException, E
    {
        try
        {
            return getFunc.get();
        }
        catch(IOException ecc)
        {
            close();
            throw ecc;
        }
    }
    private void get(streamActionInterface getFunc) throws IOException
    {
        try
        {
            getFunc.invoke();
        }
        catch(IOException ecc)
        {
            close();
            throw ecc;
        }
    }

    @FunctionalInterface
    private interface streamActionInterface
    {
        void invoke() throws IOException;
    }

    @FunctionalInterface
    private interface getInterface<T, E extends Exception>
    {
        T get() throws IOException, E;
    }


}

