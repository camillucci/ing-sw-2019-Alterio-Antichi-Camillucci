package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Bottleneck;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public abstract class AdrenalineClient implements ICallbackAdrenalineClient
{
    protected View view;
    protected String serverName;
    protected int serverPort;
    protected Bottleneck bottleneck = new Bottleneck();

    public AdrenalineClient(String serverName, int serverPort, View view)
    {
        this.view = view;
        this.serverName = serverName;
        this.serverPort = serverPort;
        bottleneck.exceptionGenerated.addEventHandler((a, exception) -> onExceptionGenerated(exception));
        setupView();
    }

    protected void onExceptionGenerated(Exception exception){
        view.getCurViewElement().onNewMessage("Disconnected");
    }

    protected abstract void setupView();

    public void start() throws IOException, NotBoundException {
        connect();
        view.getLogin().login();
    }

    protected abstract void connect() throws IOException, NotBoundException;
    protected abstract void notifyColor(int colorIndex) throws IOException, ClassNotFoundException;
    protected abstract void notifyName(String name) throws IOException, ClassNotFoundException;

    @Override
    public void newActions(List<RemoteAction> newActions) throws RemoteException {
        //todo
    }

    @Override
    public void matchStart(MatchSnapshot matchSnapshot) throws RemoteException {
        modelChanged(matchSnapshot);
    }

    @Override
    public void newMessage(String message) throws RemoteException {
        view.getCurViewElement().onNewMessage(message);
    }

    @Override
    public void modelChanged(MatchSnapshot matchSnapshot) throws RemoteException {
        view.getCurViewElement().onModelChanged(matchSnapshot);
    }

    /*
    public void loginFinto(int interf, int zero) throws IOException, ClassNotFoundException {
        messenger.askInterface();
        notifyInterface(interf);
        List<String> availableColors = getAvailableColors();
        messenger.askColor(availableColors);
        notifyColor(zero);
        messenger.askGameLenght();
        notifyGameLength(zero);
    }
 \
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
    /*
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
        initializeAction(options.get(choice)); //communicates choice to server
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
        options.get(choice).initializeAction(server); //communicates choice to server
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
