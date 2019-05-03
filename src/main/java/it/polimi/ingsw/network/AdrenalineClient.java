package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.MessengerCLI;
import it.polimi.ingsw.view.cli.ParserCLI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdrenalineClient
{
    private Client server;
    private ParserCLI parser;
    private MessengerCLI messanger;
    private final String hostname = "127.0.0.1";
    private final int ip = 10000;
    private boolean onTurn;

    public AdrenalineClient() {
        parser = new ParserCLI();
        messanger = new MessengerCLI();
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

        ArrayList<PlayerColor> takenColors = server.in().getObject(); //get available colors
        ArrayList<PlayerColor> availableColors = new ArrayList<PlayerColor>();
        for(PlayerColor pc : PlayerColor.values()) {
            if(!(takenColors.contains(pc)))
                availableColors.add(pc);
        }
        messanger.askColor(availableColors);
        //TODO lock
        server.out().sendObject(parser.parseColor(availableColors)); //send user's color of choice
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

    private void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
    }
}
