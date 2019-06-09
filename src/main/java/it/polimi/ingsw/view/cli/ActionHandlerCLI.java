package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.view.ActionHandler;

import java.io.IOException;
import java.util.List;

public class ActionHandlerCLI extends ActionHandler {
    private RemoteAction action;
    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException
    {
        CLIMessenger.displayActions(options);
        action = options.get(CLIParser.parser.parseActions(options.size()));
        notifyChoice(action.choose());
        notifyChoice(action.askActionData());
    }

    @Override
    public void updateActionData(RemoteAction.Data data) throws IOException {
        action.updateData(data);
        onActionDataUpdated();
    }

    protected void onActionDataUpdated() throws IOException
    {
        RemoteAction.Data data = action.getData();
        CLIMessenger.displayTargets(data.possiblePlayers, data.possibleSquares, data.possiblePowerUps, data.discardablePowerUps, data.canBeDone);
        int index = CLIParser.parser.parseActions(data.possiblePlayers.size() + data.possibleSquares.size() + data.possiblePowerUps.size() + data.discardablePowerUps.size() + (data.canBeDone ? 1 : 0));

        if(index < data.possiblePlayers.size())
            notifyChoice(action.addTargetPlayer(data.possiblePlayers.get(index)));
        else if(index < data.possiblePlayers.size() + data.possibleSquares.size())
            notifyChoice(action.addTargetSquare(data.possibleSquares.get(index - data.possiblePlayers.size())));
        else if(index < data.possiblePlayers.size() + data.possibleSquares.size() + data.possiblePowerUps.size())
            notifyChoice(action.usePowerUp(data.possiblePowerUps.get(index - data.possiblePlayers.size() - data.possibleSquares.size())));
        else if(index < data.possiblePlayers.size() + data.possibleSquares.size() + data.possiblePowerUps.size() + data.discardablePowerUps.size())
            notifyChoice(action.addDiscardable(data.discardablePowerUps.get(index - data.possiblePlayers.size() - data.possibleSquares.size() - data.possiblePowerUps.size())));
        else {
            notifyChoice(action.doAction());
            ((Event<ActionHandler, RemoteAction>)actionDoneEvent).invoke(this, action);
            return;
        }
        notifyChoice(action.askActionData());
    }

    private void notifyChoice(Command<RemoteActionsHandler> command)
    {
        ((Event<ActionHandler, Command<RemoteActionsHandler>>)newCommand).invoke(this, command);
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
