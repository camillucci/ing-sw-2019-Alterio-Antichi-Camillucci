package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer <T extends Remote, V extends Remote> implements RMIConnection<T, V>, NotifiedConnection<V> {
    private final IEvent<NotifiedConnection, V> newClientConnectedEvent = new Event<>();
    private final IEvent<NotifiedConnection, V> newClientDisconnectedEvent = new Event<>();
    private V client;
    private T server;
    private boolean connected;
    private Logger logger = Logger.getLogger("RMIServer");

    public RMIServer(T server)
    {
        this.server = server;
    }

    @Override
    public IEvent<NotifiedConnection, V> connectedEvent() {
        return newClientConnectedEvent;
    }

    @Override
    public IEvent<NotifiedConnection, V> disconnectedEvent() {
        return newClientDisconnectedEvent;
    }

    @Override
    public T connect(V client) throws IOException {
        if(connected)
            throw new IOException();
        this.client = client;
        ((Event<NotifiedConnection, V>)newClientConnectedEvent).invoke(this, client);
        return server;
    }

    public void close()
    {
        try {
            UnicastRemoteObject.unexportObject(server, false);
            UnicastRemoteObject.unexportObject(this, false);
        }
        catch(NoSuchObjectException e){
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    public V client()
    {
        return client;
    }

    public T server()
    {
        return server;
    }
}
