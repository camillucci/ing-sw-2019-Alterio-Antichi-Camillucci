package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.AdrenalineClient;

public class AdrenalineLauncher
{
    AdrenalineClient client;
    public AdrenalineLauncher(boolean GUI, boolean socket)
    {

    }

    public void run(String serverName, int serverPort, boolean socket) throws Exception
    {
       // client = socket ? new AdrenalineClientSocket(serverName, serverPort) : new AdrenalineClientRMI(serverName, serverPort);
        //client.login();
    }
}
