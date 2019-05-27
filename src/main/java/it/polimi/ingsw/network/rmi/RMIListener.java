package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIListener <T extends Remote, V extends Remote>
{
    public final IEvent<RMIListener, RMIServer<T,V>> newClientEvent = new Event<>();
    public final IEvent<RMIListener, RMIServer<T,V>> clientDisconnectedEvent = new Event<>();
    private List<RMIServer<T,V>> connectedHosts = new ArrayList<>();
    public final int port;
    private Logger logger;
    private Supplier<T> remoteSupplier;
    private T curRemote;
    private Registry registry;
    private static final String SERVER = "Server";

    public RMIListener(int port, Supplier<T> remoteSupplier) throws RemoteException
    {
        this.port = port;
        this.remoteSupplier = remoteSupplier;
        this.registry = LocateRegistry.createRegistry(port);
    }

    public synchronized void start()
    {
        try {
            if (registry == null)
                registry = LocateRegistry.createRegistry(1099);
            if (curRemote == null)
                newRemote();
        }
        catch (RemoteException e)
        {
            logger.log(Level.WARNING, "RemoteException, Class RMIListener, Line 45", e);
        }
    }

    public synchronized void stop() {
        if(registry == null)
            return;
        try
        {
            registry.unbind(SERVER);
            UnicastRemoteObject.unexportObject(registry, true);
            registry = null;
            curRemote = null;
        }
        catch (NotBoundException | RemoteException e)
        {
            logger.log(Level.WARNING, "Exception, Class RMIListener, Line 59", e);
        }
    }

    public synchronized List<RMIServer<T,V>> getConnected()
    {
        return new ArrayList<>(connectedHosts);
    }

    public void export(Remote object)
    {
        try{
            UnicastRemoteObject.exportObject(object, port);
        }
        catch(RemoteException e){logger.log(Level.WARNING, "ExportException, Class RMIListener, Line 76", e);}
    }

    private void newRemote()
    {
        try
        {
            //System.setProperty("java.rmi.server.hostname","127.0.0.1");
            curRemote = remoteSupplier.get();
            RMIServer<T,V> tmp = new RMIServer<>(curRemote);
            tmp.connectedEvent().addEventHandler((server, client) -> newClientConnected((RMIServer<T,V>)server));
            UnicastRemoteObject.exportObject(curRemote, port);
            Remote stub = UnicastRemoteObject.exportObject(tmp, port);
            LocateRegistry.getRegistry(port).rebind(SERVER, stub);
            registry.lookup(SERVER);
        }
        catch(IOException | NotBoundException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void newClientConnected(RMIServer<T, V> server)
    {
        server.disconnectedEvent().addEventHandler((rmiserver, b) -> onDisconnection((RMIServer<T,V>)rmiserver));
        connectedHosts.add(server);
        ((Event<RMIListener, RMIServer<T,V>>) newClientEvent).invoke(this, server);
        newRemote();
    }

    private synchronized void onDisconnection (RMIServer<T, V> server)
    {
        this.connectedHosts.remove(server);
        ((Event<RMIListener,RMIServer<T,V>>)this.clientDisconnectedEvent).invoke(this, server);
    }
}
