package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.MessengerCLI;
import it.polimi.ingsw.view.cli.ParserCLI;

import java.io.IOException;

public class AdrenalineClient
{
    private Client server;
    private ParserCLI parser;
    private MessengerCLI messanger;
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
    public void login() throws Exception {
        messanger.askConnection();
        boolean connectionType = parser.parseChoice();
        server.out().sendBool(connectionType);

        messanger.askInterface();
        boolean interfaceType = parser.parseChoice();
        server.out().sendBool(interfaceType);
        /***/
        String name;
        do {
            do {
                messanger.insertName();
                name = parser.parseName();
            } while (name == null);
            server.out().sendObject(name);
        }while(server.in().getBool()); // name is ok?
    }
}