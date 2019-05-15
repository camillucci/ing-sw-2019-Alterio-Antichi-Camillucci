package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.rmi.IRemoteAdrenalineClient;
import it.polimi.ingsw.view.cli.CLIMessenger;
import it.polimi.ingsw.view.cli.CLIParser;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class AdrenalineClient implements IRemoteAdrenalineClient
{
    public final IEvent<AdrenalineClient, MatchSnapshot> matchSnapshotUpdatedEvent = new Event<>();
    protected CLIParser parser;
    protected CLIMessenger messenger;
    protected static final String HOSTNAME = "127.0.0.1";
    protected static final int IP = 10000;
    protected MatchSnapshot matchSnapshot;

    public AdrenalineClient() {
        messenger = new CLIMessenger();
        parser = new CLIParser(messenger);
    }

    protected abstract void notifyInterface(int choice) throws IOException;
    protected abstract List<String> getAvailableColors() throws IOException, ClassNotFoundException;
    protected abstract void notifyColor(int colorIndex) throws IOException;
    protected abstract boolean notifyName(String name) throws IOException;
    protected abstract void notifyGameLength(int gameLength) throws IOException;
    protected abstract void notifyGameMap(int choice) throws IOException;
    protected abstract void notifyHandleAction(int selection, int extra) throws IOException;
    protected abstract void inizialize(RemoteAction remoteAction);
    public abstract void connect() throws IOException, NotBoundException;


    @Override
    public void newMessage(String message) throws RemoteException {
        //todo
    }

    public void setMatchSnapshot(MatchSnapshot matchSnapshot) {
        this.matchSnapshot = matchSnapshot;
        ((Event<AdrenalineClient, MatchSnapshot>)matchSnapshotUpdatedEvent).invoke(this, matchSnapshot);
    }

    public void login() throws Exception {
        messenger.askInterface();
        int choice = parser.parseChoice();
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
        }while(!notifyName(name)); // name is ok?

        List<String> availableColors = getAvailableColors();
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

    public void loginFinto(int interf, int zero) throws IOException, ClassNotFoundException {
        messenger.askInterface();
        notifyInterface(interf);
        List<String> availableColors = getAvailableColors();
        messenger.askColor(availableColors);
        notifyColor(zero);
        messenger.askGameLenght();
        notifyGameLength(zero);
    }

    /*
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


    public void updateView() throws Exception {
        //todo remove all socket-dependet code
        matchSnapshot = server.in().getObject();
        messenger.updateView(matchSnapshot);
    }
    */

    public void updateView(MatchSnapshot matchSnapshot) throws Exception {
        this.matchSnapshot = matchSnapshot;
        messenger.updateView(matchSnapshot);
    }

    public MatchSnapshot getMatchSnapshot() {
        return matchSnapshot;
    }

    public void chooseAction(ArrayList<RemoteAction> options) throws Exception {
        messenger.displayActions(options); //displays actions available
        int choice = parser.parseIndex(options.size()); //get's user action of choice
        while(choice == -1) {
            messenger.incorrectInput();
            messenger.displayActions(options);
            choice = parser.parseIndex(options.size());
        }
        inizialize(options.get(choice)); //communicates choice to server
        ArrayList<String> targetPlayers = (ArrayList<String>) options.get(choice).getPossiblePlayers();
        ArrayList<String> targetSquares = (ArrayList<String>) options.get(choice).getPossibleSquares(); //gets targets relative to chosen action
        messenger.displayTargets(targetPlayers, targetSquares); //displays targets available
        int index = parser.parseIndex(targetPlayers.size() + targetSquares.size()); //user's target of choice
        while (index == -1) {
            messenger.incorrectInput();
            messenger.displayTargets(targetPlayers, targetSquares);
            index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
        }
        if (index < targetPlayers.size())
            options.get(choice).addTargetPlayer(targetPlayers.get(index));
        else
            options.get(choice).addTargetSquare(targetSquares.get(index - targetPlayers.size()));
        while(!(options.get(choice).canBeDone())) {
            targetPlayers = (ArrayList<String>) options.get(choice).getPossiblePlayers();
            targetSquares = (ArrayList<String>) options.get(choice).getPossibleSquares();
            messenger.displayTargets(targetPlayers, targetSquares);
            index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
            while (index == -1) {
                messenger.incorrectInput();
                messenger.displayTargets(targetPlayers, targetSquares);
                index = parser.parseIndex(targetPlayers.size() + targetSquares.size());
            }
            if (index < targetPlayers.size())
                options.get(choice).addTargetPlayer(targetPlayers.get(index));
            else
                options.get(choice).addTargetSquare(targetSquares.get(index - targetPlayers.size()));
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
                options.get(choice).addTargetPlayer(targetPlayers.get(index));
            else if(index >= targetPlayers.size() && index < targetPlayers.size() + targetSquares.size())
                options.get(choice).addTargetSquare(targetSquares.get(index - targetPlayers.size()));
            else {
                options.get(choice).doAction(); //communicates choice to server
                doneAction = true;
            }
        }
    }

    /*
    public void chooseAction() throws Exception {
        //todo remove all socket-dependet code
        ArrayList<RemoteActionSocket> options = server.in().getObject(); //gets list of RemoteActionSocket
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

     */
}
