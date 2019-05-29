package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AdrenalineServerRMI extends AdrenalineServer {
    private ICallbackAdrenalineClient client;
    private RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listener;

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
    }
}
