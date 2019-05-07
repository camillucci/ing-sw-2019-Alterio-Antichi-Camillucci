package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.InputInterface;
import it.polimi.ingsw.generics.OutputInterface;
import it.polimi.ingsw.network.Client;

import java.nio.channels.NotYetConnectedException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient extends Client {

    private RMIInputOutputInterface server;

    public RMIClient(RMIInputOutputInterface server) throws RemoteException
    {
        if(!server.isConnected())
            throw new NotYetConnectedException();
        this.server = server;
    }

    @Override
    public InputInterface in() throws Exception {
        return server;
    }

    @Override
    public OutputInterface out() throws Exception {
        return server;
    }

    public static RMIClient connect(String hostname) throws RemoteException, NotBoundException
    {
        Registry registry = LocateRegistry.getRegistry(hostname);
        for(String s : registry.list())
            System.out.println(s);
        //RMIInputOutputInterface stub = (RMIInputOutputInterface) registry.lookup("Server");
        //stub.connect();
        return null;
    }

    @Override
    public void close() {

    }
}
