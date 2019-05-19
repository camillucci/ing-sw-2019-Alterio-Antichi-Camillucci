package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.AdrenalineServer;
import java.rmi.RemoteException;

public class AdrenalineServerRMI extends AdrenalineServer {
    private ICallbackAdrenalineClient client;
    private RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listener;

    @Override
    protected void sendMessage(String message) throws RemoteException {
        client.newMessage(message);
    }

    public void initialize(ICallbackAdrenalineClient client)
    {
        this.client = client;
    }
}
