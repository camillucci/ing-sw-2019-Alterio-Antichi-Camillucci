package it.polimi.ingsw.network.rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClient <T extends RMIConnection & Remote, V extends Remote>
{
    public final T server;
    public final V client;
    Logger logger;
    private boolean unExportServer;

    protected RMIClient(T server, V client, boolean unExportServer)
    {
        this.server = server;
        this.client = client;
        this.unExportServer = unExportServer;
    }

    public void close()
    {
        try
        {
            if(unExportServer)
                UnicastRemoteObject.unexportObject(server, false);
            else
                UnicastRemoteObject.unexportObject(client, false);
        }
        catch (RemoteException e)
        {
            //logger.log(Level.WARNING, "Exception, Class RMIClient, Line 39", e);
        }
    }
    public static <T extends RMIConnection & Remote, V extends Remote> RMIClient <T, V> connect(String hostname, int port, V toExport) throws IOException, NotBoundException
    {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        T stub  = (T) registry.lookup("Server");
        UnicastRemoteObject.exportObject(toExport, 0);
        stub.connect(toExport);
        return new RMIClient<>(stub, toExport, false);
    }
}
