package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.socket.RemoteActionSocket;
import it.polimi.ingsw.view.ActionHandler;
import it.polimi.ingsw.view.Login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerCLI extends ActionHandler {
    private RemoteAction action;
    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
        CLIMessenger.displayActions((ArrayList<RemoteAction>) options);
        int choice = CLIParser.parser.parseIndex(options.size());
        ((Event<ActionHandler, Integer>)choiceEvent).invoke(this, choice);
        action = options.get(choice);
        ArrayList<String> targetPlayers = (ArrayList<String>) action.getPossiblePlayers();
        ArrayList<String> targetSquares = (ArrayList<String>) action.getPossibleSquares();
        CLIMessenger.displayTargets(targetPlayers, targetSquares); //displays targets available
        int index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size()); //user's target of choice
        while (index == -1) {
            CLIMessenger.incorrectInput();
            CLIMessenger.displayTargets(targetPlayers, targetSquares);
            index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size());
        }
        if (index < targetPlayers.size())
            action.addTargetPlayer(targetPlayers.get(index));
        else
            action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
        while(!(action.canBeDone())) {
            targetPlayers = (ArrayList<String>) action.getPossiblePlayers();
            targetSquares = (ArrayList<String>) action.getPossibleSquares();
            CLIMessenger.displayTargets(targetPlayers, targetSquares);
            index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size());
            while (index == -1) {
                CLIMessenger.incorrectInput();
                CLIMessenger.displayTargets(targetPlayers, targetSquares);
                index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size());
            }
            if (index < targetPlayers.size())
                action.addTargetPlayer(targetPlayers.get(index));
            else
                action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
        }

        boolean doneAction = false;
        while(!doneAction) {
            CLIMessenger.displayTargetsAndAction(targetPlayers, targetSquares);
            index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + 1);
            while(index == -1) {
                CLIMessenger.incorrectInput();
                CLIMessenger.displayTargetsAndAction(targetPlayers, targetSquares);
                index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + 1);
            }
            if (index < targetPlayers.size())
                action.addTargetPlayer(targetPlayers.get(index));
            else if(index >= targetPlayers.size() && index < targetPlayers.size() + targetSquares.size())
                action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
            else {
                action.doAction(); //communicates choice to server
                doneAction = true;
            }
        }
    }

    @Override
    public void onNewMessage(String message) {
        //TODO
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        //TODO
    }
}
