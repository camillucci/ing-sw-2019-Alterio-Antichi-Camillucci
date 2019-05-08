package it.polimi.ingsw.network;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.util.ArrayList;
import java.util.List;

public abstract class AdrenalineClient
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

    protected abstract void notifyInterface(int choice);
    protected abstract List<String> getAvaibleColors();
    protected abstract void notifyColor(int colorIndex);
    protected abstract boolean isOk(String name);
    protected abstract void notifyGameLength(int gameLength);
    protected abstract void notifyGameMap(int choice);

    public void login() throws Exception {
        int choice = parser.parseChoice();
        // already chosen RMI or Socket
        /*
        messenger.askConnection();
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

         */

        messenger.askInterface();
        choice = parser.parseChoice();
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.askInterface();
            choice = parser.parseChoice();
        }
        notifyInterface(choice);
        /***/
        String name;
        do {
            do {
                messenger.insertName();
                name = parser.parseName();
            } while (name == null);
            server.out().sendObject(name);
        }while(!isOk(name)); // name is ok?

        List<String> availableColors = getAvaibleColors();
        messenger.askColor(availableColors);
        //TODO lock
        int index = parser.parseIndex(availableColors.size());
        while(index == -1) {
            messenger.incorrectInput();
            messenger.askColor(availableColors);
            index = parser.parseIndex(availableColors.size());
        }
        notifyColor(index);//send user's color of choice
        messenger.askGameLenght();
        choice = parser.parseGameLenght();
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.askGameLenght();
            choice = parser.parseGameLenght();
        }
        notifyGameLength(choice);
        messenger.askGameMap();
        parser.parseGameMap();
        choice = parser.parseGameMap();
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.askGameMap();
            choice = parser.parseGameMap();
        }
        notifyGameMap(choice);
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
        //todo remove all socket-dependet code
        matchSnapshot = server.in().getObject();
        messenger.updateView(matchSnapshot);
    }

    public MatchSnapshot getMatchSnapshot() {
        return matchSnapshot;
    }

    public void chooseAction() throws Exception {
        //todo remove all socket-dependet code
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
        while(!(options.get(choice).canBeDone())) {
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
        while(!doneAction) {
            messenger.displayTargetsAndAction(targetPlayers, targetSquares);
            index = parser.parseIndex(targetPlayers.size() + targetSquares.size() + 1);
            while(index == -1) {
                messenger.incorrectInput();
                messenger.displayTargetsAndAction(targetPlayers, targetSquares);
                index = parser.parseIndex(targetPlayers.size() + targetSquares.size() + 1);
            }
            if (index < targetPlayers.size())
                options.get(choice).addTarget(targetPlayers.get(index));
            else if(index >= targetPlayers.size() && index < targetPlayers.size() + targetSquares.size())
                options.get(choice).addTarget(targetSquares.get(index - targetPlayers.size()));
            else {
                options.get(choice).doAction(); //communicates choice to server
                doneAction = true;
            }
        }
    }
}
