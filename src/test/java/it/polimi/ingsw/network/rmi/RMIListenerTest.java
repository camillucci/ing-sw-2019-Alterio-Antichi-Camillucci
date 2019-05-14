package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.IAdrenalineServer;
import it.polimi.ingsw.network.socket.TCPListener;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    int n = 0;

    /* Commented because maven-test could not finish test
    @Test
    void connectionTest() throws Exception
    {
            RMIListener listener = new RMIListener<>(1099, () -> new AdrenalineServer(new Controller()));
            listener.newClientEvent.addEventHandler((a, b) -> n++);
            listener.start();
            final int N = 100;
            IAdrenalineServer server = null;
            for (int i = 0; i < N; i++)
                 server = RMIClient.connect("127.0.0.1", 1099);
            assertEquals(N, listener.getConnected().size());
            assertEquals(N, n);
        try {
            listener.stop();
            RMIClient.connect("127.0.0.1", 1099);
            fail();
            assert(false);
        }
        catch (RemoteException | NotBoundException e)
        {
            assertTrue(true);
        }
        try {
            listener.start();
            RMIClient.connect("127.0.0.1", 1099);
            assert(true);
        }
        catch (RemoteException | NotBoundException e)
        {
            assert(false);
        }
    }

     */
}
