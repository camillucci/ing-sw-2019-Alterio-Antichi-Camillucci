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

public class RMIListener <T extends NotifiedConnection & RMIConnection & Remote, V extends Remote>
{
    public final IEvent<RMIListener, RMIClient<T,V>> newClientEvent = new Event<>();
    public final IEvent<RMIListener, T> clientDisconnectedEvent = new Event<>();
    private List<RMIClient<T,V>> connectedHosts = new ArrayList<>();
    public final int port;
    private Logger logger;
    private Supplier<T> remoteSupplier;
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
        if(registry == null)
            return;
        try
        {
            registry.unbind("Server");
            for(RMIClient c : connectedHosts)
                c.close();
            UnicastRemoteObject.unexportObject(registry, false);
            registry = null;
            curRemote = null;
        }
        catch (NotBoundException | RemoteException e)
        {
            logger.log(Level.WARNING, "Exception, Class RMIListener, Line 59", e);
        }
    }

    public synchronized List<RMIClient<T,V>> getConnected()
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
            curRemote.connectedEvent().addEventHandler((remote, client) -> newClientConnected((T)remote, (V)client));
            Remote stub = UnicastRemoteObject.exportObject(curRemote, port);
            LocateRegistry.getRegistry(port).rebind("Server", stub);
        }
        catch(ExportException e)
        {
            logger.log(Level.WARNING, "ExportException, Class RMIListener, Line 55", e);
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "RemoteException, Class RMIListener, Line 58", e);
        }
    }

    private void newClientConnected(T server, V client)
    {
        //server.disconnectedEvent().addEventHandler((rmiClient, b) -> onDisconnection(server));
        RMIClient<T,V> tmp;
        connectedHosts.add(tmp = new RMIClient<>(server, client, true));
        ((Event<RMIListener, RMIClient<T,V>>) newClientEvent).invoke(this, tmp);
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
