package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIInputOutput implements RMIInputOutputInterface {

    Event<RMIInputOutput, Object> connectedEvent = new Event<>();
    Event<RMIInputOutput, Object> failEvent = new Event<>();

    private boolean connected = false;
    private int readPos = 0;
    private int writePos = 1;
    private Serializable[] object = new Serializable[2];

    @Override
    public boolean isConnected()
    {
        return connected;
    }

    @Override
    public void connect()
    {
        this.connected = true;
        readPos = 1;
        writePos = 0;
        this.connectedEvent.invoke(this, null);
    }

    @Override
    public byte[] getBytes() throws Exception {
        return new byte[0];
    }

    @Override
    public <T> T getObject() throws Exception {
        return (T) this.object[readPos];
    }

    @Override
    public void getFile(String filename) throws Exception {

    }

    @Override
    public int getInt() throws Exception {
        return 0;
    }

    @Override
    public long getLong() throws Exception {
        return 0;
    }

    @Override
    public boolean getBool() throws Exception {
        return false;
    }

    @Override
    public void sendBytes(byte[] bytes) throws Exception {

    }

    @Override
    public void sendObject(Serializable object) throws Exception {
        this.object[writePos] = object;
    }

    @Override
    public void sendFile(String fileName) throws Exception {

    }

    @Override
    public void sendInt(int val) throws Exception {

    }

    @Override
    public void sendLong(long val) throws Exception {

    }

    @Override
    public void sendBool(boolean bool) throws Exception {

    }

    private <T, E extends Exception> T get(GetInterface<T, E> getFunc) throws RemoteException, E
    {
        try
        {
            return getFunc.get();
        }
        catch(RemoteException ecc)
        {
            failEvent.invoke(this, null);
            throw ecc;
        }
    }

    private void get(StreamActionInterface getFunc) throws RemoteException
    {
        try
        {
            getFunc.invoke();
        }
        catch(RemoteException ecc)
        {
            failEvent.invoke(this, null);
            throw ecc;
        }
    }

    @FunctionalInterface
    private interface StreamActionInterface
    {
        void invoke() throws RemoteException;
    }

    @FunctionalInterface
    private interface GetInterface<T, E extends Exception>
    {
        T get() throws RemoteException, E;
    }
}
