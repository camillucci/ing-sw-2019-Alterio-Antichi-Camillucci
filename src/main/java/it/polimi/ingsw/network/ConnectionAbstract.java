package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

public abstract class ConnectionAbstract implements IConnection {
    public final IEvent<ConnectionAbstract, Object> connectedEvent = new Event<>();
    public final IEvent<ConnectionAbstract, Object> disconnectedEvent = new Event<>();
    private boolean connected = false;

    @Override
    public void connect(Runnable func) throws Exception
    {
        if(!connected) {
            ((Event<ConnectionAbstract, Object>)this.connectedEvent).invoke(this, null);
            func.run();
            connected = true;
        }
    }
}
