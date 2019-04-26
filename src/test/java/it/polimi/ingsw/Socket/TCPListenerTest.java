package it.polimi.ingsw.Socket;

import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPListenerTest {

    int port = 9999;
    int maxConnected = 7;
    String localHost = "127.0.0.1";
    boolean connected = false;

    @Test
    void start()
    {
        TCPListener listener = new TCPListener(port, 3);
        try
        {
            listener.start();
            Socket socket = new Socket(localHost, port);
            assert(true);
        }
        catch(Exception ecc)
        {
            assert(false);
        }
        finally { listener.stop(); }
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
        TCPListener listener = new TCPListener(port, maxConnected);
        try
        {
            listener.newClientEvent.addEventHandler((a,b)->{
                try{
                    listener.stop();
                    assertFalse(tryConnect());
                    assertFalse(listener.isListening());
                    listener.start();
                    tryConnect();
                }
                catch(Exception ecc) {assert (false);}
            });
            listener.start();
            tryConnect();
            while(listener.getConnected().size() < maxConnected)
                ;
            assertFalse(tryConnect());
            assertFalse(listener.isListening());
            assertTrue(listener.getConnected().size() == maxConnected);
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally { listener.stop(); }
    }

    @Test
    void threadSpam()
    {
        TCPListener listener = new TCPListener(port, maxConnected);
        try
        {
            final int N = 100;
            Thread[] threads = new Thread[N];
            for(int i=0; i < N; i++)
                (threads[i] = new Thread(()-> {
                    try{
                        for(int j=0; j < N ; j++) {
                            listener.start();
                            listener.isListening();
                            listener.getConnected();
                            listener.stop();
                        }
                    }
                    catch(Exception e){ assert (false);}
                })).start();
            for(Thread t : threads)
                t.join();
            listener.stop();
            assertFalse(listener.isListening());
            assertFalse(tryConnect());
            listener.start();
            assertTrue(tryConnect());

        }
        catch (Exception ecc){assert (false);}
        finally { listener.stop(); }
    }

    @Test
    void getConnected()
    {

    }
}