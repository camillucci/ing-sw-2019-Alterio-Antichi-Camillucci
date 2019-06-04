package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.IRMIAdrenalineServer;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AdrenalineServerRMI extends AdrenalineServer implements IRMIAdrenalineServer {

    public final IEvent<AdrenalineServerRMI, Object> newClientConnected = new Event<>();
    private ICallbackAdrenalineClient client;
    private Registry registry;
    private boolean stopPinging = false;
    private final Thread pingingThread = new Thread(() -> bottleneck.tryDo( () -> {
        while(!getStopPinging()) {
            client.ping();
            Thread.sleep(PING_PERIOD);
        }
    }));

    @Override
    protected void createActionHandler(Player curPlayer) throws RemoteException {
        remoteActionsHandler = new RMIRemoteActionHandler(match, curPlayer);
        UnicastRemoteObject.exportObject(remoteActionsHandler, 0);
        client.setRemoteActionHandler(remoteActionsHandler);
    }

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
    }

    @Override
    protected void sendMessage(String message) throws RemoteException {
        client.newMessage(message);
    }

    @Override
    protected void notifyMatchChanged(MatchSnapshot matchSnapshot) throws IOException {
        client.modelChanged(matchSnapshot);
    }

    @Override
    protected void newActions(List<RemoteAction> actions) throws IOException, ClassNotFoundException {
        client.newActions(actions);
    }

    public void initialize(ICallbackAdrenalineClient client)
    {
        this.client = client;
        startPinging();
    }

    @Override
    public void registerClient(ICallbackAdrenalineClient client) {
        this.client = client;
        ((Event<AdrenalineServerRMI, Object>)newClientConnected).invoke(this, null);
    }

    private synchronized boolean getStopPinging() {
        return stopPinging;
    }

    private synchronized void setStopPinging(boolean stopPinging) {
        this.stopPinging = stopPinging;
    }

    @Override
    protected void startPinging()
    {
        if(pingingThread.getState() != Thread.State.TERMINATED)
            return;
        setStopPinging(false);
        pingingThread.start();
    }

    @Override
    protected void stopPinging()
    {
        if(pingingThread.getState() == Thread.State.TERMINATED)
            return;
        setStopPinging(true);
        try {
            pingingThread.join();
        } catch (InterruptedException e) {
            //todo
        }
    }

    @Override
    public void ping() {
        // called by client to test connection
    }
}
