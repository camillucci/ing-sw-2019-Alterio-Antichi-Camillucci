package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdrenalineServerRMI extends AdrenalineServer implements IRMIAdrenalineServer {

    public final IEvent<AdrenalineServerRMI, Object> newClientConnected = new Event<>();
    private ICallbackAdrenalineClient client;
    private Registry registry;
    private boolean stopPinging = false;
    private static final Logger logger = Logger.getLogger("AdrenalineServerRMI");

    private final Thread pingingThread = new Thread(() -> bottleneck.tryDo( () -> {
        while(!getStopPinging()) {
            client.ping();
            Thread.sleep(PING_PERIOD);
        }
    }));

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
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
        startPinging();
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
        if(pingingThread.getState() == Thread.State.TERMINATED || pingingThread.getState() == Thread.State.NEW) {
            setStopPinging(false);
            pingingThread.start();
        }
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
            logger.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    protected void sendCommand(Command<View> command) throws IOException {
        client.newViewCommand(command);
    }

    @Override
    public void ping() {
        // called by client to test connection
    }
}
