package it.polimi.ingsw.network.rmi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RMIListenerTest
{
    @Test
    void test1() throws Exception
    {
        RMIListener listener = new RMIListener();
        listener.start();

        RMIClient client = RMIClient.connect("127.0.0.1");

    }

}