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
            serverRMI.newClientConnectedEvent.addEventHandler((a, b) -> onNewClientConnected(serverRMI));
            serverRMI.clientDisconnectedEvent.addEventHandler((client, a) -> onClientDisconnected(client));
            Remote stub = UnicastRemoteObject.exportObject(serverRMI, port );
            LocateRegistry.getRegistry(port).rebind("Server", stub);
        } catch (RemoteException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    private synchronized void onClientDisconnected(AdrenalineServerRMI client) {
        connectedList.remove(client);
    }

    @SuppressWarnings("squid:S2276")
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
            } catch (RemoteException e) {
                stopPinging = true;
            } catch (InterruptedException e) {
                stopPinging = true;
                Thread.currentThread().interrupt();
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

        setStopPinging();
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    private synchronized void setStopPinging(){
        this.stopPinging = true;
    }

    private synchronized boolean getStopPinging(){
        return stopPinging;
    }

    private void onNewClientConnected(AdrenalineServerRMI serverRMI){
        addConnected(serverRMI);
        newClient();
    }

    public void stop() throws NoSuchObjectException {
        UnicastRemoteObject.unexportObject(registry, true);
    }

    public void closeAll() {
        for(AdrenalineServerRMI client : getConnected())
            client.close();
    }
}
