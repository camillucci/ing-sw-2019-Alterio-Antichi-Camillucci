package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;
import java.rmi.RemoteException;

public class AdrenalineServerRMI extends AdrenalineServer {
    private ICallbackAdrenalineClient client;
    private RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listener;

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
    }

    @Override
    protected void sendMessage(String message) throws RemoteException {
        client.newMessage(message);
    }

    @Override
    protected void notifyMatchStarted(MatchSnapshot matchSnapshot) throws RemoteException {
        client.matchStart(matchSnapshot);
    }

    public void initialize(ICallbackAdrenalineClient client)
    {
        this.client = client;
    }
}
