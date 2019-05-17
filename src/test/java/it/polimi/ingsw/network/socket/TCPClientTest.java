package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;

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
                    clientSocket.out().sendObject(message);
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
            String messageRecived = clientSocket.in().getObject();

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
    void sendBool_GetBool()
    {
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
            // Server sends bytes to client
            try
            {
                clientSocket.out().sendBool(false);
                clientSocket.out().sendBool(true);
                clientSocket.out().sendBool(false);

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
            assertFalse(serverSocket.in().getBool());
            assertTrue(serverSocket.in().getBool());
            assertFalse(serverSocket.in().getBool());
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
                    clientSocket.out().sendBytes(message);

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
            byte[] messageRecived = clientSocket.in().getBytes();

            for(int i=0; i <  messageRecived.length; i++)
                assertEquals(message[i], messageRecived[i]);
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
    void sendFile_GetFile()
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
                    clientSocket.out().sendFile(path);
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

            // receiving and saving file
            clientSocket.in().getFile(path2);

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
        Match match = new Match(Arrays.asList("A", "B", "C"), Arrays.asList(PlayerColor.BLUE, PlayerColor.GREY, PlayerColor.VIOLET), 5, 11);
        match.gameBoard.addKillShotTrack(Collections.singletonList(PlayerColor.GREY));
        match.gameBoard.getSquares().get(0).addPlayer(match.gameBoard.getPlayers().get(0));
        match.gameBoard.getPlayers().get(0).addWeapon(match.gameBoard.weaponDeck.draw());
        match.gameBoard.getPlayers().get(0).addWeapon(match.gameBoard.weaponDeck.draw());
        match.gameBoard.getPlayers().get(0).unloadWeapon(match.gameBoard.getPlayers().get(0).getLoadedWeapons().get(0));
        match.gameBoard.getPlayers().get(0).addPowerUpCard();
        match.gameBoard.getPlayers().get(1).addDamage(match.gameBoard.getPlayers().get(2), 1);
        match.gameBoard.getPlayers().get(2).addMark(match.gameBoard.getPlayers().get(1), 2);
        TCPListener listener = new TCPListener(listeningPort, maxConnected);
        try
        {
            MatchSnapshot matchSnapshot = match.createSnapshot(0);
            listener.newClientEvent.addEventHandler(((tcpListener, clientSocket) -> {
                // Server sends bytes to client
                try
                {
                    clientSocket.out().sendObject(matchSnapshot);
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
            MatchSnapshot received = clientSocket.in().getObject();

            // Assert Equals
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].north, received.gameBoardSnapshot.squareSnapshots[0][0].north);
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].south, received.gameBoardSnapshot.squareSnapshots[0][0].south);
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].west, received.gameBoardSnapshot.squareSnapshots[0][0].west);
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].east, received.gameBoardSnapshot.squareSnapshots[0][0].east);
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].getColors(), received.gameBoardSnapshot.squareSnapshots[0][0].getColors());
            assertEquals(matchSnapshot.gameBoardSnapshot.squareSnapshots[0][0].getCards(), received.gameBoardSnapshot.squareSnapshots[0][0].getCards());
            assertEquals(matchSnapshot.gameBoardSnapshot.mapType, received.gameBoardSnapshot.mapType);
            assertEquals(matchSnapshot.gameBoardSnapshot.skulls, received.gameBoardSnapshot.skulls);
            assertEquals(matchSnapshot.gameBoardSnapshot.getKillShotTrack(), received.gameBoardSnapshot.getKillShotTrack());

            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).color, received.getPublicPlayerSnapshot().get(0).color);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).name, received.getPublicPlayerSnapshot().get(0).name);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).skull, received.getPublicPlayerSnapshot().get(0).skull);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).blueAmmo, received.getPublicPlayerSnapshot().get(0).blueAmmo);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).redAmmo, received.getPublicPlayerSnapshot().get(0).redAmmo);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).yellowAmmo, received.getPublicPlayerSnapshot().get(0).yellowAmmo);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).finalFrenzy, received.getPublicPlayerSnapshot().get(0).finalFrenzy);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).getDamage(), received.getPublicPlayerSnapshot().get(0).getDamage());
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).getMark(), received.getPublicPlayerSnapshot().get(0).getMark());
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).getUnloadedWeapons(), received.getPublicPlayerSnapshot().get(0).getUnloadedWeapons());
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).loadedWeaponsNumber, received.getPublicPlayerSnapshot().get(0).loadedWeaponsNumber);
            assertEquals(matchSnapshot.getPublicPlayerSnapshot().get(0).powerUpsNumber, received.getPublicPlayerSnapshot().get(0).powerUpsNumber);

            assertEquals(matchSnapshot.privatePlayerSnapshot.getLoadedWeapons(), received.privatePlayerSnapshot.getLoadedWeapons());
            assertEquals(matchSnapshot.privatePlayerSnapshot.getPowerUps(), received.privatePlayerSnapshot.getPowerUps());
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
