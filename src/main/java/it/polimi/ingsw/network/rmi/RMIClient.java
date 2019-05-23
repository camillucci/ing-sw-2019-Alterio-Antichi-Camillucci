package it.polimi.ingsw.network.rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class RMIClient <T extends Remote, V extends Remote>
{
    public final T server;
    public final V client;
    Logger logger;

    protected RMIClient(T server, V client)
    {
        this.server = server;
        this.client = client;
    }

    public void close()
    {
        try
        {
            UnicastRemoteObject.unexportObject(client, false);
        }
        catch (RemoteException e)
        {
            //logger.log(Level.WARNING, "Exception, Class RMIClient, Line 39", e);
        }
    }

    public static <T extends Remote, V extends Remote> RMIClient <T, V> connect(String hostname, int port, V client) throws IOException, NotBoundException
    {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        RMIConnection<T,V> stub  = (RMIConnection<T,V>) registry.lookup("Server");
        UnicastRemoteObject.exportObject(client, 0);
        T tmp = stub.connect(client);
        return new RMIClient<>(tmp, client);
    }
}
