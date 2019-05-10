package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.IConnection;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    public static <T extends IConnection & Remote> T connect(String hostname, int port) throws Exception, RemoteException, NotBoundException
    {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        T stub  = (T) registry.lookup("Server");
        stub.connect();
        return stub;
    }
}
