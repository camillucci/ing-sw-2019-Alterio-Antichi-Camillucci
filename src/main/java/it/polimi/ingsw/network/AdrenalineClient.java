package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.util.ArrayList;

public class AdrenalineClient
{
    private Client server;
    private CLIParser parser;
    private CLIMessenger messanger;
    private static final String HOSTNAME = "127.0.0.1";
    private static final int IP = 10000;

    public AdrenalineClient() {
        parser = new CLIParser();
        messanger = new CLIMessenger();
    }

    public void login() throws Exception {
        messanger.askConnection();
        boolean connectionType = parser.parseChoice();
        if(!connectionType)
            server = TCPClient.connect(HOSTNAME, IP);
        else {
            //server = RMIClient.connect(HOSTNAME, IP);
            server = null;
        }

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

        ArrayList<String> availableColors = server.in().getObject();
        messanger.askColor(availableColors);
        //TODO lock
        server.out().sendInt(parser.parseIndex(availableColors)); //send user's color of choice
        if(server.in().getBool()) {
            messanger.askGameLenght();
            parser.parseGameLenght();
            messanger.askGameMap();
            parser.parseGameMap();
        }
    }

    private void matchStart() throws Exception {
        messanger.threePlayers();
        if(server.in().getBool())
            messanger.matchStart();
    }

    public void spawn() throws Exception {
        ArrayList<String> powerupCards = server.in().getObject(); //Get two powerup cards
        messanger.spawn(powerupCards);
        server.out().sendInt(parser.parseSpawnChoice(powerupCards));
    }
}
