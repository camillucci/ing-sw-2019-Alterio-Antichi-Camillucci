package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PrivatePlayerSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.util.ArrayList;

public class AdrenalineClient
{
    private Client server;
    private CLIParser parser;
    private CLIMessenger messenger;
    private static final String HOSTNAME = "127.0.0.1";
    private static final int IP = 10000;
    private MatchSnapshot matchSnapshot;

    public AdrenalineClient() {
        messenger = new CLIMessenger();
        parser = new CLIParser(messenger);
    }

    public void login() throws Exception {
        messenger.askConnection();
        int choice = parser.parseChoice();
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.askConnection();
            choice = parser.parseChoice();
        }
        if(choice == 0)
            server = TCPClient.connect(HOSTNAME, IP);
        else {
            //server = RMIClient.connect(HOSTNAME, IP);
            server = null;
        }

        messenger.askInterface();
        choice = parser.parseChoice();
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.askInterface();
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
                messenger.insertName();
                name = parser.parseName();
            } while (name == null);
            server.out().sendObject(name);
        }while(server.in().getBool()); // name is ok?

        ArrayList<String> availableColors = server.in().getObject();
        messenger.askColor(availableColors);
        //TODO lock
        int index = parser.parseIndex(availableColors.size());
        while(index == -1) {
            messenger.incorrectInput();
            messenger.askColor(availableColors);
            index = parser.parseIndex(availableColors.size());
        }
        server.out().sendInt(index); //send user's color of choice
        if(server.in().getBool()) {
            messenger.askGameLenght();
            choice = parser.parseGameLenght();
            while(choice == -1) {
                messenger.incorrectInput();
                messenger.askGameLenght();
                choice = parser.parseGameLenght();
            }
            messenger.askGameMap();
            parser.parseGameMap();
            server.out().sendInt(choice);
            choice = parser.parseGameMap();
            while(choice == -1) {
                messenger.incorrectInput();
                messenger.askGameMap();
                choice = parser.parseGameMap();
            }
            server.out().sendInt(choice);
        }
    }

    private void matchStart() throws Exception {
        messenger.threePlayers();
        if(server.in().getBool())
            messenger.matchStart();
    }

    private void handleTargets() {

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
        messenger.updateView(matchSnapshot);
    }

    public MatchSnapshot getMatchSnapshot() {
        return matchSnapshot;
    }

    public void chooseAction() throws Exception {
        ArrayList<RemoteAction> options = server.in().getObject(); //gets list of RemoteAction
        messenger.displayActions(options); //displays actions available
        int choice = parser.parseIndex(options.size()); //get's user action of choice
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.displayActions(options);
            choice = parser.parseIndex(options.size());
        }
        options.get(choice).inizialize(server); //communicates choice to server
        ArrayList<PublicPlayerSnapshot> targetPlayers = (ArrayList<PublicPlayerSnapshot>) options.get(choice).getPossiblePlayers();
        ArrayList<SquareSnapshot> targetSquares = (ArrayList<SquareSnapshot>) options.get(choice).getPossibleSquares(); //gets targets relative to chosen action
        messenger.displayTargets(targetPlayers, targetSquares); //displays targets available
        int index = parser.parseIndex(targetPlayers.size() + targetSquares.size()); //user's target of choice
        while (index == -1) {
            messenger.incorrectInput();
            messenger.displayTargets(targetPlayers, targetSquares);
            index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
        }
        if (index < targetPlayers.size())
            options.get(choice).addTarget(targetPlayers.get(index));
        else
            options.get(choice).addTarget(targetSquares.get(index - targetPlayers.size()));
        while(!(options.get(choice).getCanBeDone())) {
            targetPlayers = (ArrayList<PublicPlayerSnapshot>) options.get(choice).getPossiblePlayers();
            targetSquares = (ArrayList<SquareSnapshot>) options.get(choice).getPossibleSquares();
            messenger.displayTargets(targetPlayers, targetSquares);
            index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
            while (index == -1) {
                messenger.incorrectInput();
                messenger.displayTargets(targetPlayers, targetSquares);
                index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
            }
            if (index < targetPlayers.size())
                options.get(choice).addTarget(targetPlayers.get(index));
            else
                options.get(choice).addTarget(targetSquares.get(index - targetPlayers.size()));
        }
        boolean doneAction = false;
        while(!(doneAction)) {
            //TODO
        }
        options.get(choice).doAction(); //communicates choice to server
    }
}
