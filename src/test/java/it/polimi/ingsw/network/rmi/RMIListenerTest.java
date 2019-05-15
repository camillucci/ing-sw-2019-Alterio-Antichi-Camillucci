package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.Controller;
import org.junit.jupiter.api.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    int n = 0;
    @Test
    void connectionTest() throws Exception
    {
            RMIListener listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
            listener.newClientEvent.addEventHandler((a, b) -> n++);
            listener.start();
            final int N = 100;
            for (int i = 0; i < N; i++)
            {
                AdrenalineClientRMI clientRMI = new AdrenalineClientRMI("127.0.0.1", 1099);
                clientRMI.connect();
            }
            assertEquals(N, listener.getConnected().size());
            assertEquals(N, n);
        try {
            listener.stop();
            AdrenalineClientRMI clientRMI = new AdrenalineClientRMI("127.0.0.1", 1099);
            clientRMI.connect();
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
            AdrenalineClientRMI clientRMI = new AdrenalineClientRMI("127.0.0.1", 1099);
            clientRMI.connect();
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
}
