package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.rmi.RemoteException;

public abstract class ConnectionAbstract implements IConnection {
    public final IEvent<ConnectionAbstract, Object> connectedEvent = new Event<>();
    public final IEvent<ConnectionAbstract, Object> disconnectedEvent = new Event<>();
    private boolean connected = false;

    @Override
    public void connect(Runnable func) throws RemoteException
    {
        if(!connected) {
            ((Event<ConnectionAbstract, Object>)this.connectedEvent).invoke(this, null);
            func.run();
            connected = true;
        }
    }
}
