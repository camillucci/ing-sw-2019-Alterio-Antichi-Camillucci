package it.polimi.ingsw.Socket;

import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPListenerTest {

    int port = 9999;
    int maxConnected = 3;
    String localHost = "127.0.0.1";
    TCPListener listener;

    private void createListener()
    {
        listener = new TCPListener(port, 3);
    }

    @Test
    void start()
    {
        try
        {
            createListener();
            listener.start();
            Socket socket = new Socket(localHost, port);
            assert(true);
        }
        catch(Exception ecc)
        {
            assert(false);
        }
    }

    boolean tryConnect()
    {
        try
        {
            Socket socket = new Socket(localHost, port);
            return true;
        }
        catch(Exception ecc)
        {
            return false;
        }
    }

    @Test
    void isListening()
    {
        try
        {
            createListener();
            int timeout = 3000;
            for(int i = 0; i < maxConnected; i++)
            {
                listener.start();
                tryConnect();
                for(int j=0; ; Thread.sleep(200))
                    if(j < timeout) {
                        if (listener.getConnected().size() == i+1)
                        {
                            assert(true);
                            break;
                        }
                    }
                    else {
                        assert (false);
                        return;
                    }
                listener.stop();
                assertFalse(tryConnect());
                assertFalse(listener.isListening());
            }

            assertFalse(listener.isListening());
            listener.start();
            assertFalse(listener.isListening());
            assertFalse(tryConnect());

        }
        catch(Exception ecc)
        {
            assert (false);
        }
    }

    @Test
    void getConnected()
    {

    }
}