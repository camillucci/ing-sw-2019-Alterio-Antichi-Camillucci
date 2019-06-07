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

        CLIMessenger.displayActions(options);
        ((Event<ActionHandler, RemoteAction>)choiceEvent).invoke(this, action = options.get(CLIParser.parser.parseActions(options.size())));
        while(!askChoice(action, action.canBeDone())) // while !doAction
            ;
    }

    private boolean askChoice(RemoteAction action, boolean canBeDone) throws IOException, ClassNotFoundException {
        List<String> targetPlayers = action.getPossiblePlayers();
        List<String> targetSquares = action.getPossibleSquares();
        List<String> usablePowerUps = action.getPossiblePowerups();
        List<String> discardablePowerUps = action.getDiscardablePowerups();

        CLIMessenger.displayTargets(targetPlayers, targetSquares, usablePowerUps, discardablePowerUps);
        int index = CLIParser.parser.parseActions(targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size() + (canBeDone ? 1 : 0));

        if(index < targetPlayers.size())
            action.addTargetPlayer(targetPlayers.get(index));
        else if(index < targetPlayers.size() + targetSquares.size())
            action.addTargetSquare(targetSquares.get(index - targetPlayers.size()));
        else if(index < targetPlayers.size() + targetSquares.size() + usablePowerUps.size())
            action.usePowerUp(usablePowerUps.get(index - targetPlayers.size() - targetSquares.size()));
        else if(index < targetPlayers.size() + targetSquares.size() + usablePowerUps.size() + discardablePowerUps.size())
            action.addDiscardable(discardablePowerUps.get(index - targetPlayers.size() - targetSquares.size() - usablePowerUps.size()));
        else {
            action.doAction();
            return true;
        }
        return false;
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
