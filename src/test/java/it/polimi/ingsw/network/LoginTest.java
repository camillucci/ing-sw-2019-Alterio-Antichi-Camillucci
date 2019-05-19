package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.AdrenalineLauncherClient;
import it.polimi.ingsw.controller.AdrenalineLauncherServer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.rmi.*;
import it.polimi.ingsw.view.cli.CLIParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
public class LoginTest {
    private final boolean REMOTE_TESTING = false;
    private boolean triggered = false;

    @Test
    void loginSocket_CLI() throws IOException, NotBoundException {
        String userInput = "turangla_lella\n1\n5\n10\n";
        CLIParser.setInputStream(new ByteArrayInputStream(userInput.getBytes()));// set Input stream for CLIParser to userInput String
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(true, 10000);
        server.start();
        AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, true, "127.0.0.1", 10000);
        client.start();
        server.stop();
    }

    @Test
    void LoginRMI_CLI() throws IOException, NotBoundException {
        String userInput = "turangla_lella\n1\n5\n10\n";
        CLIParser.setInputStream(new ByteArrayInputStream(userInput.getBytes())); // set Input stream for CLIParser to userInput String
        AdrenalineLauncherServer server = new AdrenalineLauncherServer(false, 10001);
        server.start();
        AdrenalineLauncherClient client = new AdrenalineLauncherClient(false, false, "127.0.0.1", 10001);
        client.start();
        server.stop();
    }
    /*

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

        while(listener.getConnected().isEmpty())
            ;
        listener.stop();
    }

    @Test
    void callbackClient() throws Exception {
        if(!REMOTE_TESTING)
            return;

        AdrenalineClientRMI adrenalineClient = new AdrenalineClientRMI();
        RMIClient<IAdrenalineServer, AdrenalineClientRMI> client = RMIClient.connect("127.0.0.1",1099, adrenalineClient);
        adrenalineClient.initialize(client.server);
        while(!triggered)
            ;
    }

     */
}
