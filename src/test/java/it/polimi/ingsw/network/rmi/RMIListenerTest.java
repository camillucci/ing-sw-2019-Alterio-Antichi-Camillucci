package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.Client;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    boolean triggered;
    @Test
    void test1() throws Exception
    {
        RMIListener listener = new RMIListener();
        listener.newClientEvent.addEventHandler((a,b)-> this.triggered = true);
        listener.start();

        Client client = RMIClient.connect("127.0.0.1");
        client = RMIClient.connect("127.0.0.1");
        client = RMIClient.connect("127.0.0.1");
        client = RMIClient.connect("127.0.0.1");

        assertTrue(listener.getConnected().size() == 4);
        assert(triggered);
    }
}