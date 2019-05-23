package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.AdrenalineLauncherClient;
import it.polimi.ingsw.network.IAdrenalineServer;
import it.polimi.ingsw.view.cli.CLIParser;
import it.polimi.ingsw.view.cli.CLIView;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    int n = 0;

    @Test
    void connectionTest() throws Exception
    {
            RMIListener<IAdrenalineServer, ICallbackAdrenalineClient> listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
            RMIClient<IAdrenalineServer, ICallbackAdrenalineClient> client;

            listener.newClientEvent.addEventHandler((a, b) -> n++);
            listener.start();
            final int N = 100;
            for (int i = 0; i < N; i++)
                client = RMIClient.connect("127.0.0.1", 1099, new AdrenalineClientRMI("127.0.0.1", 10000, new CLIView()));
            assertEquals(N, listener.getConnected().size());
            assertEquals(N, n);
        try {
            listener.stop();
            client =  RMIClient.connect("127.0.0.1", 1099, new AdrenalineClientRMI("127.0.0.1", 10000, new CLIView()));
            fail();
            assert(false);
        }
        catch (RemoteException | NotBoundException e)
        {
            assertTrue(true);
        }
        finally {
            listener.stop();
        }
        try {
            listener.start();
            client = RMIClient.connect("127.0.0.1", 1099, new AdrenalineClientRMI("127.0.0.1", 10000, new CLIView()));
            assert(true);
        }
        catch (RemoteException | NotBoundException e)
        {
            assert(false);
        }
        finally {
            listener.stop();
        }
    }

    /*
    @Test
    void client() {
        System.setProperty("java.rmi.server.hostname","169.254.87.91");
        try {
            LocateRegistry.createRegistry(25377);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        String userInput = "turangla_lella3\n1\n";
        CLIParser.parser.setInputStream(new ByteArrayInputStream(userInput.getBytes()));
        AdrenalineLauncherClient client = null;
        try {
            client = new AdrenalineLauncherClient(false, false, "169.254.87.91", 25377);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    */
}
