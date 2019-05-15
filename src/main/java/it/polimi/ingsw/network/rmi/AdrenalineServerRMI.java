package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class AdrenalineServerRMI extends AdrenalineServer implements IAdrenalineServerRMI, NotifiedConnection
{
    public final IEvent<NotifiedConnection, Remote> connectedEvent = new Event<>();
    public final IEvent<NotifiedConnection, Remote> disconnectedEvent = new Event<>();
    private boolean connected = false;
    private IRemoteAdrenalineClient client;

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
        viewUpdatEvent.addEventHandler((a,match)->sendView(match));
    }

    public IRemoteAdrenalineClient getClient()
    {
        return client;
    }

    private void sendView(MatchSnapshot matchSnapshot)
    {
        try{
            this.client.newMatchSnapshot(matchSnapshot);
        }
        catch(RemoteException e){}
    }

    @Override
    public <T extends Remote>  void  connect(T client) throws RemoteException {
        if(!connected) {
            connected = true;
            this.client = (IRemoteAdrenalineClient)client;
            ((Event<NotifiedConnection, Remote>)this.connectedEvent).invoke(this, client);
        }
        else
            updateView(null);
    }

    @Override
    public IEvent<NotifiedConnection, Remote> connectedEvent() {
        return connectedEvent;
    }

    @Override
    public IEvent<NotifiedConnection, Remote> disconnectedEvent() {
        return disconnectedEvent;
    }
}
