package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.View;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AdrenalineClientRMI extends AdrenalineClient implements ICallbackAdrenalineClient {
    private IRMIAdrenalineServer server;
    private IActionHandler remoteActionHandler;
    private boolean stopPinging = true;
    private final Thread pingingThread = new Thread(() -> bottleneck.tryDo( () -> {
        while(!getStopPinging()) {
            server.ping();
            Thread.sleep(PING_PERIOD);
        }
    }));

    public AdrenalineClientRMI(String serverName, int serverPort, View view) {
        super(serverName, serverPort, view);
    }
    public void initialize(IRMIAdrenalineServer server)
    {
        this.server = server;
    }

    @Override
    public void setRemoteActionHandler(IActionHandler remoteActionHandler) throws RemoteException {
        this.remoteActionHandler = remoteActionHandler;
    }

    @Override
    public void ping() {
        // called by server periodically to test connection
    }

    @Override
    protected void connect() throws IOException, NotBoundException {
        UnicastRemoteObject.exportObject(this, 2000);
        server = (IRMIAdrenalineServer) LocateRegistry.getRegistry(serverName, this.serverPort).lookup("Server");
        server.registerClient(this);
    }

    @Override
    protected void startPing() {
        if(pingingThread.getState() != Thread.State.TERMINATED)
            return;
        setStopPinging(false);
        pingingThread.start();
    }

    @Override
    protected void notifyName(String name) throws IOException {
        boolean accepted = server.setName(name);
        view.getLogin().notifyAccepted(accepted);
        if(accepted)
            view.getLogin().notifyAvailableColor(server.availableColors());
    }

    @Override
    protected void notifyColor(int colorIndex) throws IOException {
        if(!server.setColor(colorIndex))
            view.getLogin().notifyAvailableColor(server.availableColors());
        else {
            boolean isHost = server.isHost();
            if (isHost) {
                view.getLogin().gameMapEvent.addEventHandler((a, map) -> bottleneck.tryDo(() -> server.ready()));
                view.getLogin().notifyHost(true);
            } else {
                view.getLogin().notifyHost(false);
                server.ready();
            }
        }
    }

    @Override
    protected void notifyGameLength(int gameLength) throws IOException {
        server.setGameLength(gameLength);
    }

    @Override
    protected void notifyGameMap(int gameMap) throws IOException {
        server.setGameMap(gameMap);
    }

    @Override
    protected void stopPing()
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

    private synchronized void setStopPinging(boolean value){
        stopPinging = value;
    }
    private synchronized boolean getStopPinging(){
        return stopPinging;
    }

    @Override
    public void newActions(List<RemoteAction> newActions) throws IOException, ClassNotFoundException {
        view.getActionHandler().chooseAction(new ArrayList<>(newActions));
    }

    @Override
    protected void initializeAction(RemoteAction action) throws RemoteException {
        ((RemoteActionRMI)action).initialize(remoteActionHandler); //communicates choice to server
    }
}
