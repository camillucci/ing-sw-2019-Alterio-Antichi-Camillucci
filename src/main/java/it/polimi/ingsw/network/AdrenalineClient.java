package it.polimi.ingsw.network;

import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.CLIMessanger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.util.ArrayList;

public class AdrenalineClient
{
    private Client server;
    private CLIParser parser;
    private CLIMessanger messanger;
    private final String hostname = "127.0.0.1";
    private final int ip = 10000;
    private boolean onTurn;

    public AdrenalineClient() {
        parser = new CLIParser();
        messanger = new CLIMessanger();
    }

    public void login() throws Exception {
        messanger.askConnection();
        boolean connectionType = parser.parseChoice();
        if(!connectionType)
            server = TCPClient.connect(hostname, ip);
        else {
            //server = RMIClient.connect(hostname, ip);
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
        //TODO add timer method
        if(server.in().getBool())
            messanger.matchStart();
    }

    public void Spawn() throws Exception {
        server.in().getObject(); //Get two powerup cards
        //TODO
    }

    private void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
    }
}
