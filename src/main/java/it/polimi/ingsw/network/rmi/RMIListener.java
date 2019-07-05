package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.socket.TCPClient;

import java.io.IOException;
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
    private List<AdrenalineServerRMI> connectedList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger("RMIListener");
    private Thread pingingThread;
    private boolean stopPinging;

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

    public synchronized void pingAll(int period){
        if(pingingThread != null && pingingThread.getState() != Thread.State.TERMINATED)
            return;

        pingingThread = new Thread(() -> {
            try {
                while (!getStopPinging()) {
                    for(AdrenalineServerRMI client : getConnected())
                        client.pingClient();
                    Thread.sleep(period);
                }
            } catch (InterruptedException e) {
                //todo logger
                stopPinging = true;
            }
        });
        pingingThread.start();
    }

    public synchronized List<AdrenalineServerRMI> getConnected() {
        return new ArrayList<>(connectedList);
    }

    public synchronized void addConnected(AdrenalineServerRMI connected)
    {
        connectedList.add(connected);
    }

    public synchronized void stopPinging(){
        if(pingingThread == null || pingingThread.getState() == Thread.State.TERMINATED)
            return;

        setStopPinging(true);
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
    private synchronized void setStopPinging(boolean stopPinging){
        this.stopPinging = stopPinging;
    }

    private synchronized boolean getStopPinging(){
        return stopPinging;
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
    }

    public void closeAll() throws NoSuchObjectException {
        for(IRMIAdrenalineServer client : getConnected())
            closeClient(client);
    }
}
