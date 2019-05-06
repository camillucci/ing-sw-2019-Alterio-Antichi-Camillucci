package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
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
    private MatchSnapshot matchSnapshot;

    public AdrenalineClient() {
        messanger = new CLIMessenger();
        parser = new CLIParser(messanger);
    }

    public void login() throws Exception {
        messanger.askConnection();
        int choice = parser.parseChoice();
        while(choice == -1) {
            messanger.incorrectInput();
            messanger.askConnection();
            choice = parser.parseChoice();
        }
        if(choice == 0)
            server = TCPClient.connect(HOSTNAME, IP);
        else {
            //server = RMIClient.connect(HOSTNAME, IP);
            server = null;
        }

        messanger.askInterface();
        choice = parser.parseChoice();
        while(choice == -1) {
            messanger.incorrectInput();
            messanger.askInterface();
            choice = parser.parseChoice();
        }
        if(choice == 0)
            server.out().sendBool(true);
        else
            server.out().sendBool(false);
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
        int index = parser.parseIndex(availableColors);
        while(index == -1) {
            messanger.incorrectInput();
            messanger.askColor(availableColors);
            index = parser.parseIndex(availableColors);
        }
        server.out().sendInt(index); //send user's color of choice
        if(server.in().getBool()) {
            messanger.askGameLenght();
            choice = parser.parseGameLenght();
            while(choice == -1) {
                messanger.incorrectInput();
                messanger.askGameLenght();
                choice = parser.parseGameLenght();
            }
            messanger.askGameMap();
            parser.parseGameMap();
            server.out().sendInt(choice);
            choice = parser.parseGameMap();
            while(choice == -1) {
                messanger.incorrectInput();
                messanger.askGameMap();
                choice = parser.parseGameMap();
            }
            server.out().sendInt(choice);
        }
    }

    private void matchStart() throws Exception {
        messanger.threePlayers();
        if(server.in().getBool())
            messanger.matchStart();
    }

    /*
    public void spawn() throws Exception {
        ArrayList<String> powerupCards = server.in().getObject(); //Get two powerup cards
        messanger.spawn(powerupCards);
        server.out().sendInt(parser.parseSpawnChoice(powerupCards));
    }
     */

    public void updateView() throws Exception {
        matchSnapshot = server.in().getObject();
        messanger.updateView(matchSnapshot);
    }

    public MatchSnapshot getMatchSnapshot() {
        return matchSnapshot;
    }
}
