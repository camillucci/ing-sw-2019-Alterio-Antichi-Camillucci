package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.ConnectionAbstract;

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

public class RMIListener <T extends ConnectionAbstract & Remote>
{
    public final IEvent<RMIListener, T> newClientEvent = new Event<>();
    public final IEvent<RMIListener, T> clientDisconnectedEvent = new Event<>();
    private List<T> connectedHosts = new ArrayList<>();
    public final int port;
    private Logger logger;
    private Supplier<T> remoteSupplier;
    private List<Remote> exported = new ArrayList<>();
    private T curRemote;
    private Registry registry;

    public RMIListener(int port, Supplier<T> remoteSupplier) throws RemoteException
    {
        this.port = port;
        this.remoteSupplier = remoteSupplier;
        registry = LocateRegistry.createRegistry(port);
    }

    public synchronized void start()
    {
        try {
            if (registry == null) {
                registry = LocateRegistry.createRegistry(1099);
                newRemote();
            }
            else if (curRemote == null)
                newRemote();
        }
        catch (RemoteException e){logger.log(Level.WARNING, "RemoteException, Class RMIListener, Line 45", e);}
    }

    public synchronized void stop() {
        try
        {
            registry.unbind("Server");
            for(Remote r : exported)
                UnicastRemoteObject.unexportObject(r, false);
            UnicastRemoteObject.unexportObject(registry, false);
            registry = null;
            curRemote = null;
        }
        catch (NotBoundException | RemoteException e)
        {
            logger.log(Level.WARNING, "Exception, Class RMIListener, Line 59", e);
        }
    }

    public synchronized List<T> getConnected()
    {
        return new ArrayList<>(connectedHosts);
    }

    public void export(Remote object)
    {
        try{
            UnicastRemoteObject.exportObject(object, port);
            this.exported.add(object);
        }
        catch(RemoteException e){logger.log(Level.WARNING, "ExportException, Class RMIListener, Line 76", e);}
    }

    private void newRemote()
    {
        try
        {
            //System.setProperty("java.rmi.server.hostname","127.0.0.1");
            curRemote = remoteSupplier.get();
            curRemote.connectedEvent.addEventHandler((remote, b) -> newClientConnected((T)remote));
            Remote stub = UnicastRemoteObject.exportObject(curRemote, port);
            LocateRegistry.getRegistry(port).rebind("Server", stub);
        }
        catch(ExportException e)
        {
            logger.log(Level.WARNING, "ExportException, Class RMIListener, Line 55", e);
        }
        catch (RemoteException e) {
            logger.log(Level.WARNING, "RemoteException, Class RMIListener, Line 58", e);
        }
    }

    private void newClientConnected(T client)
    {
        exported.add(client);
        client.disconnectedEvent.addEventHandler((rmiClient, b) -> onDisconnection(client));
        connectedHosts.add(client);
        ((Event<RMIListener, T>) newClientEvent).invoke(this, client);
        newRemote();
    }

    private synchronized void onDisconnection (T client)
    {
        this.connectedHosts.remove(client);
        ((Event<RMIListener,T>)this.clientDisconnectedEvent).invoke(this, client);
    }

    private void tryUnexport(Remote obj){
        try {
            if(obj != null)
                UnicastRemoteObject.unexportObject(obj, false);
        } catch (NoSuchObjectException e) {
            //logger.log(Level.WARNING, "NoSuchObjectException, Class RMIListener, Line 85", e);
        }
    }
}
