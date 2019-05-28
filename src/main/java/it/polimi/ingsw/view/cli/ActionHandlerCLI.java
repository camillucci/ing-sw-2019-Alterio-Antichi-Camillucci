package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.view.ActionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionHandlerCLI extends ActionHandler {

    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException {
        RemoteAction action;
        ArrayList<String> targetPlayers;
        ArrayList<String> targetSquares;
        ArrayList<String> usablePowerUps;
        ArrayList<String> discardablePowerUps;
        int index;

        CLIMessenger.displayActions((ArrayList<RemoteAction>) options);
        int choice = CLIParser.parser.parseIndex(options.size());
        ((Event<ActionHandler, Integer>)choiceEvent).invoke(this, choice);
        action = options.get(choice);
        do {
            targetPlayers = (ArrayList<String>) action.getPossiblePlayers();
            targetSquares = (ArrayList<String>) action.getPossibleSquares();
            usablePowerUps = (ArrayList<String>) action.getPossiblePowerups();
            discardablePowerUps = (ArrayList<String>) action.getDiscardablePowerups();
            CLIMessenger.displayTargets(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps); //displays targets available
            index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size()); //user's target of choice
            while (index == -1) {
                CLIMessenger.incorrectInput();
                CLIMessenger.displayTargets(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps);
                index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size());
            }
            if(index < targetPlayers.size())
                action.addTargetPlayer(targetPlayers.get(index));
            else if(index < targetPlayers.size() + targetSquares.size())
                action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
            else if(index < targetPlayers.size() + targetSquares.size() + usablePowerUps.size())
                action.usePowerUp(usablePowerUps.get(index - targetPlayers.size() - targetSquares.size()));
            else
                action.addDiscardable(discardablePowerUps.get(index - targetPlayers.size() - targetSquares.size() - usablePowerUps.size()));
        }
        while(!(action.canBeDone()));

        boolean doneAction = false;
        while(!doneAction) {
            CLIMessenger.displayTargetsAndAction(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps);
            index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size() + 1);
            while(index == -1) {
                CLIMessenger.incorrectInput();
                CLIMessenger.displayTargetsAndAction(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps);
                index = CLIParser.parser.parseIndex(targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size() + 1);
            }
            if (index < targetPlayers.size())
                action.addTargetPlayer(targetPlayers.get(index));
            else if(index < targetPlayers.size() + targetSquares.size())
                action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
            else if(index < targetPlayers.size() + targetSquares.size() + usablePowerUps.size())
                action.usePowerUp(usablePowerUps.get(index - targetPlayers.size() - targetSquares.size()));
            else if(index < targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size())
                action.addDiscardable(discardablePowerUps.get(index - targetPlayers.size() - targetSquares.size() - usablePowerUps.size()));
            else {
                action.doAction(); //communicates choice to server
                doneAction = true;
            }
        }
    }

    @Override
    public void onNewMessage(String message) {
        CLIMessenger.printMessage(message);
    }

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        CLIMessenger.updateView(matchSnapshot);
    }
}
