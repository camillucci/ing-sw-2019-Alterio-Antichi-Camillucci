package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.SquareBorder;
import it.polimi.ingsw.model.SquareSnapshot;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TCPClientTest {

    private int listeningPort = 9999;
    private String localHost = "127.0.0.1";
    private int maxConnected = 3;
    private TCPClient clientSocket;

    @Test
    void close()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            listener.start();
            TCPClient.connect(localHost, listeningPort);
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
        finally {
            listener.stop();
        }
    }

    @Test
    void sendMessageAuto_getMessageAuto()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            String message = "Hello World!";
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.out.sendObject(message);
                    clientSocket.close();
                }
                catch(Exception ecc)
                {
                    assert (false);
                }
            }));

            listener.start();
            // client connecting to server
            clientSocket = TCPClient.connect(localHost, listeningPort);

            // client getting bytes from server
            String messageRecived = clientSocket.in.getObject();

            assertEquals(message, messageRecived);
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            listener.stop();
        }
    }

    @Test
    void sendByte_GetByte()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
            // Server sends bytes to client
            try
            {
                clientSocket.out.sendByte((byte)'a');
                clientSocket.out.sendByte((byte)'b');
                clientSocket.out.sendByte((byte)'c');

                clientSocket.close();
            }
            catch(Exception ecc)
            {
                assert (false);
            }

            }));

            listener.start();
            // client connecting to server
            TCPClient serverSocket = TCPClient.connect(localHost, listeningPort);

            // client getting bytes from server
            assertEquals('a', (char)serverSocket.in.getByte());
            assertEquals('b', (char)serverSocket.in.getByte());
            assertEquals('c', (char)serverSocket.in.getByte());
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            listener.stop();
        }
    }

    @Test
    void sendBytes_GetBytes()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            byte[] message = new byte[] {(byte)'h', (byte)'e', (byte)'l', (byte)'l', (byte)'0'};
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.out.sendBytes(message);

                    clientSocket.close();
                }
                catch(Exception ecc)
                {
                    assert (false);
                }
            }));

            listener.start();

            // client connecting to server
            clientSocket = TCPClient.connect(localHost, listeningPort);

            // client getting bytes from server
            byte[] messageRecived = clientSocket.in.getBytes(message.length-2);

            for(int i=0; i <  messageRecived.length; i++)
                assertEquals(message[i], messageRecived[i]);
            assertEquals(message.length - 2, messageRecived.length);
            clientSocket.close();
            listener.stop();
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            listener.stop();
        }
    }

    @Test
    void sendBytesAuto_GetBytesAuto()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            byte[] message = new byte[] {(byte)'h', (byte)'e', (byte)'l', (byte)'l', (byte)'0'};
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.out.sendBytesAuto(message);
                    clientSocket.close();
                }
                catch(Exception ecc)
                {
                    assert (false);
                }
            }));

            listener.start();
            // client connecting to server
            clientSocket = TCPClient.connect(localHost, listeningPort);

            // client getting bytes from server
            byte[] messageRecived = clientSocket.in.getBytesAuto();

            for(int i=0; i <  messageRecived.length; i++)
                assertEquals(message[i], messageRecived[i]);
            assertEquals(message.length, messageRecived.length);

        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            listener.stop();
        }
    }

    @Test
    void sendFileAuto_GetFileAuto()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        String path = "path1.test";
        String path2 = "path2.test";
        try
        {
            if(clientSocket != null)
                clientSocket.close();
            File file = new File(path);
            file.createNewFile();

            // creating file
            FileOutputStream stream = new FileOutputStream(path);
            final int fileSize = 1024 * 1024 * 16; // 16MB
            byte[] garbage = new byte[fileSize];
            for(int i=0; i < garbage.length; i++)
                garbage[i] = (byte)(Math.random() * 230 );
            stream.write(garbage);
            stream.close();

            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    // sending file
                    clientSocket.out.sendFileAuto(path);
                    clientSocket.close();
                    file.delete();
                }
                catch(Exception ecc)
                {
                    assert (false);
                }
            }));

            listener.start();
            // client connecting to server
            clientSocket = TCPClient.connect(localHost, listeningPort);

            // reciving and saving file
            clientSocket.in.getFileAuto(path2);

            // open file in an array tmp
            byte[] tmp = new byte[fileSize];
            FileInputStream fileInputStream = new FileInputStream(path2);
            fileInputStream.read(tmp, 0, tmp.length);
            fileInputStream.close();

            // asserting equals
            assertEquals(garbage.length, tmp.length);
            for(int i=0; i < garbage.length; i++)
                assertEquals(garbage[i], tmp[i]);
            assert (true);
            clientSocket.close();
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            File file = new File(path);
            if(file.exists())
                file.delete();
            file = new File(path2);
            if(file.exists())
                file.delete();
            listener.stop();
        }
    }

    @Test
    void sendObject()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            SquareSnapshot squareSnapshot = new SquareSnapshot(new SquareBorder[]{SquareBorder.DOOR, SquareBorder.NOTHING, SquareBorder.ROOM, SquareBorder.ROOM}, PlayerColor.GREY);
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.out.sendObject(squareSnapshot);
                    clientSocket.close();
                }
                catch(Exception ecc)
                {
                    assert (false);
                }
            }));

            listener.start();
            // client connecting to server
            clientSocket = TCPClient.connect(localHost, listeningPort);

            // client getting bytes from server
            SquareSnapshot recived = clientSocket.in.getObject();

            // Assert Equals
            assertEquals(squareSnapshot.borders.length, recived.borders.length);
            for(int i=0; i < squareSnapshot.borders.length; i++)
                assertEquals(squareSnapshot.borders[i], recived.borders[i]);
            assertEquals(squareSnapshot.color, recived.color);
        }
        catch(Exception ecc)
        {
            assert (false);
        }
        finally {
            listener.stop();
        }
    }
}
