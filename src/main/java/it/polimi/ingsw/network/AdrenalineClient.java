package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.ManagerCLI;

import java.io.IOException;

public class AdrenalineClient
{
    private Client server;
    private ManagerCLI managerCLI = new ManagerCLI();
    private final String hostname = "127.0.0.1";
    private final int ip = 10000;

    public AdrenalineClient(boolean connectionType) throws IOException {
        if(!connectionType)
            server = TCPClient.connect(hostname, ip);
        else {
            //server = RMIClient.connect(hostname, ip);
            server = null;
        }
    }

    public void setName(String name) {
        //TODO server.setName(name);
    }
}
