package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.IAdrenalineServer;
import org.junit.jupiter.api.Test;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    boolean triggered;
    @Test
    void test1() throws Exception
    {
        RMIListener listener = new RMIListener<>(1099, () -> new AdrenalineServerRMI(new Controller()));
        listener.newClientEvent.addEventHandler((a,b)-> triggered=true);
        listener.start();

        final int N = 100;
        for(int i = 0; i < N; i++)
            RMIClient.connect("127.0.0.1", 1099);
        assertEquals(N, listener.getConnected().size());
    }
}