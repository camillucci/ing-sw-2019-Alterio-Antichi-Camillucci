package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.AdrenalineClient;
import it.polimi.ingsw.network.rmi.AdrenalineClientRMI;
import it.polimi.ingsw.network.socket.AdrenalineClientSocket;
import it.polimi.ingsw.network.socket.TCPClient;

import java.io.IOException;
import java.rmi.NotBoundException;

public class AdrenalineLauncher
{
    AdrenalineClient client;
    public void run(String serverName, int serverPort, boolean socket) throws Exception
    {
        client = socket ? new AdrenalineClientSocket(serverName, serverPort) : new AdrenalineClientRMI(serverName, serverPort);
        client.login();
    }
}
