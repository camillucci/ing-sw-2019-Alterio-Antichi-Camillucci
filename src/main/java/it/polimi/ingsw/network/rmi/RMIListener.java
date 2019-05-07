package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.network.Client;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIListener
{
    public final Event<RMIListener, RMIClient> newClientEvent = new Event<>();
    public final Event<RMIListener, RMIClient> clientDisconnectedEvent = new Event<>();
    private RMIInputOutput curServer;

    public synchronized void start()
    {
        if(curServer == null)
            newServer();
    }

    public synchronized void stop() {
        // TODO
    }

    public synchronized List<RMIClient> getConnected()
    {
        return new ArrayList<>(connectedHosts);
    }

    private void newServer()
    {
        try
        {
            RMIInputOutput server = new RMIInputOutput();
            server.connectedEvent.addEventHandler((rmiInputOutput, b) -> newClientConnected(rmiInputOutput));
            RMIInputOutputInterface stub = (RMIInputOutputInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            registry.bind("Server", stub);
        }
        catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private void newClientConnected(RMIInputOutput client)
    {
        try
        {
            RMIClient connected = new RMIClient(client);
            connected.disconnectedEvent.addEventHandler((rmiClient, b) -> onDisconnection((RMIClient)rmiClient));
            connectedHosts.add(connected);
            newClientEvent.invoke(this, connected);
            newServer();
        }catch(RemoteException e){}
    }
    private synchronized void onDisconnection (RMIClient client)
    {
        this.connectedHosts.remove(client);
        this.clientDisconnectedEvent.invoke(this, client);
    }

    private List<RMIClient> connectedHosts = new ArrayList<>();
}
