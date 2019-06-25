package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.view.ActionHandler;

import java.io.IOException;
import java.util.List;

/**
 * This class extends ActionHandler and is dedicated to managing actions on clients side, in case the user chose a
 * CLI based display.
 */

public class ActionHandlerCLI extends ActionHandler {
    private RemoteAction action;

    /**
     * Gets a list of possible actions and displays them to the user. Calls the other methods to make sure a
     * notification is provided to the server once a choice has been taken by the user.
     * @param options Is the list of actions available the user can choose from
     */
    @Override
    public void chooseAction(List<RemoteAction> options) throws IOException
    {
        CLIMessenger.displayActions(options);
        action = options.get(CLIParser.parser.parseIndex(options.size()));
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
        List<String> players = data.getPossiblePlayers(), squares = data.getPossibleSquares(), possiblePU = data.getPossiblePowerUps(),
                     discardablePUs = data.getDiscardablePowerUps(), discardableAmmos = data.getDiscardableAmmos(), weapons = data.getPossibleWeapons();

        CLIMessenger.displayTargets(data);
        int index = CLIParser.parser.parseActionUserChoice(data);
        if(index < players.size())
            notifyChoice(action.addTargetPlayer(players.get(index)));
        else if( (index -= players.size()) < squares.size())
            notifyChoice(action.addTargetSquare(squares.get(index)));
        else if( (index -= squares.size()) < possiblePU.size())
            notifyChoice(action.usePowerUp(possiblePU.get(index)));
        else if( (index -= possiblePU.size()) <  discardablePUs.size())
            notifyChoice(action.addDiscardable(discardablePUs.get(index)));
        else if( (index -= discardablePUs.size()) < discardableAmmos.size())
            notifyChoice(action.addDiscardableAmmo(discardableAmmos.get(index)));
        else if( (index -= discardableAmmos.size()) < weapons.size())
            notifyChoice(action.addDiscardableAmmo(weapons.get(index)));
        else {
            notifyChoice(action.doAction());
            ((Event<ActionHandler, RemoteAction>)actionDoneEvent).invoke(this, action);
            return;
        }
        notifyChoice(action.askActionData());
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
