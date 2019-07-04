package it.polimi.ingsw.network.rmi;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIListener
{
    private Supplier<AdrenalineServerRMI> supplier;
    private Registry registry;
    private int port;
    private List<IRMIAdrenalineServer> connectedList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger("RMIListener");

    public RMIListener(int port, Supplier<AdrenalineServerRMI> supplier) throws RemoteException {
        this.supplier = supplier;
        this.port = port;
        registry = LocateRegistry.createRegistry(port);
    }

    public void start(){
        newClient();
    }

    private void newClient()
    {
        try {
            AdrenalineServerRMI serverRMI = supplier.get();
            serverRMI.newClientConnected.addEventHandler((a,b) -> onNewClientConnected(serverRMI));
            Remote stub = UnicastRemoteObject.exportObject(serverRMI, port );
            LocateRegistry.getRegistry(port).rebind("Server", stub);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private void onNewClientConnected(AdrenalineServerRMI serverRMI){
        connectedList.add(serverRMI);
        newClient();
    }

    private void closeClient(IRMIAdrenalineServer serverRMI) throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(serverRMI, true);
    }

    public void stop() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(registry, true);
        for(IRMIAdrenalineServer client : connectedList)
            closeClient(client);
    }
}
