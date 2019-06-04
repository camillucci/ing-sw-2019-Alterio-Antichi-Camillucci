package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.IRMIAdrenalineServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RMIListener
{
    private Supplier<AdrenalineServerRMI> supplier;
    private Registry registry;
    private int port;
    private List<IRMIAdrenalineServer> connectedList = new ArrayList<>();

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
            Remote stub = UnicastRemoteObject.exportObject(serverRMI,port );
            LocateRegistry.getRegistry(1099).rebind("Server", stub);
        } catch (RemoteException e) {
            //todo
        }
    }

    private void onNewClientConnected(AdrenalineServerRMI serverRMI){
        connectedList.add(serverRMI);
        newClient();
    }

    public void stop(){
        //todo
    }
}
