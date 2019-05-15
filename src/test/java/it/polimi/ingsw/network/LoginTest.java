package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
public class LoginTest {
    private final boolean REMOTE_TESTING = false;
    private boolean triggered = false;

    @Test
    void callbackServer() throws Exception
    {
        if(!REMOTE_TESTING)
            return;

        RMIListener<AdrenalineServerRMI, ICallbackAdrenalineClient> listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
        listener.start();
        listener.newClientEvent.addEventHandler((a, rmiServer) -> {
            rmiServer.server().initialize(rmiServer.client());
            try {
                rmiServer.client().modelChanged(null);
            }
            catch(RemoteException e){}
        });

        listener.stop();
    }

    @Test
    void callbackClient() throws Exception {
        if(!REMOTE_TESTING)
            return;

        AdrenalineClientRMI adrenalineClient = new AdrenalineClientRMI();
        adrenalineClient.modelChangedEvent.addEventHandler((a,b) -> triggered = true);
        RMIClient<IAdrenalineServer, AdrenalineClientRMI> client = RMIClient.connect("127.0.0.1",1099, adrenalineClient);
        adrenalineClient.initialize(client.server);
        while(!triggered)
            ;
    }
}
