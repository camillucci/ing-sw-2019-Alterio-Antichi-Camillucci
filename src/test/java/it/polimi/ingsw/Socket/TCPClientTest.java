package it.polimi.ingsw.Socket;

import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPClientTest {

    int listeningPort = 9999;
    String localHost = "127.0.0.1";
    int maxConnected = 3;
    TCPListener listener = new TCPListener(listeningPort, maxConnected);

    boolean tryConnect()
    {
        try
        {
            Socket socket = new Socket(localHost, listeningPort);
            return true;
        }
        catch(Exception ecc)
        {
            return false;
        }
    }


    @Test
    void close()
    {
        try
        {
            listener.start();
            Socket socket = new Socket(localHost, listeningPort);
            TCPClient serverSocket = new TCPClient(socket);
            for(int i=0, timeout = 2000;; i += 200, Thread.sleep(200))
                if(listener.getConnected().size() > 0)
                    break;
                else if (i > timeout)
                {
                    assert(false);
                    return;
                }
            listener.getConnected().get(0).close();

            assertEquals(0, listener.getConnected().size());
        }
        catch(Exception ecc)
        {
            assert(false);
        }
    }

    @Test
    void sendByte()
    {
        try
        {
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.sendByte((byte)'a');
                    clientSocket.sendByte((byte)'b');
                    clientSocket.sendByte((byte)'c');
                }
                catch(Exception ecc)
                {

                }
            }));

            listener.start();
            // client connecting to server
            TCPClient serverSocket = new TCPClient(new Socket(localHost, listeningPort));

            // client getting bytes from server
            assertEquals('a', (char)serverSocket.getByte());
            assertEquals('b', (char)serverSocket.getByte());
            assertEquals('c', (char)serverSocket.getByte());

        }
        catch(Exception ecc)
        {

        }
    }

    @Test
    void sendByteAuto() {
    }

    @Test
    void sendBytes() {
    }

    @Test
    void sendBytesAuto() {
    }

    @Test
    void sendFile() {
    }

    @Test
    void sendFileAuto() {
    }

    @Test
    void getByte() {
    }

    @Test
    void getBytes() {
    }

    @Test
    void getBytesAuto() {
    }

    @Test
    void getFile() {
    }

    @Test
    void getFileAuto() {
    }
}