package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
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

    /**
     * Updates the remote action with the choices of the user. If a state without legal continuation is entered, a
     * rollback is automatically called.
     * @param data Choices of the user regarding the action previously selected
     */
    @Override
    public void updateActionData(RemoteAction.Data data) throws IOException {
        action.updateData(data);
        if(action.getData().isInvalid()) {
            CLIMessenger.rollbackError();
            notifyChoice(action.rollback());
        }
        onActionDataUpdated();
    }


    protected void onActionDataUpdated() throws IOException
    {
        RemoteAction.Data data = action.getData();

        List<String> players = data.getPossiblePlayers(), squares = data.getPossibleSquares(), possiblePU = data.getPossiblePowerUps(),
                     discardablePUs = data.getDiscardablePowerUps(), discardableAmmos = data.getDiscardableAmmos(), weapons = data.getPossibleWeapons();

        if(data.canBeDone && players.isEmpty() && squares.isEmpty() && possiblePU.isEmpty()
                && discardablePUs.isEmpty() && discardableAmmos.isEmpty() && weapons.isEmpty()) {
            notifyChoice(action.doAction());
            ((Event<ActionHandler, RemoteAction>)actionDoneEvent).invoke(this, action);
            return;
        }

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
            notifyChoice(action.addWeapon(weapons.get(index)));
        else {
            notifyChoice(action.doAction());
            ((Event<ActionHandler, RemoteAction>)actionDoneEvent).invoke(this, action);
            return;
        }
        notifyChoice(action.askActionData());
    }

    /**
     * Method used to communicate a generic message that the CLIMessenger class is going to display to the user
     * @param message String that represents the message that's going to eventually be displayed to the user.
     */
    @Override
    public void onNewMessage(String message) {
        CLIMessenger.printMessage(message);
    }

    /**
     * Creates and prints the message relative to a player disconnecting
     * @param name Name of  the disconnected player
     */
    @Override
    public void disconnectedPlayerMessage(String name) {
        String message = name + " left the room";
        CLIMessenger.printMessage(message);
    }

    /**
     * This method is never invoked in this class
     * @param name Name of the reconnected player
     */

    @Override
    public void reconnectedMessage(String name) {
        String message = name + " is back";
        CLIMessenger.printMessage(message);
    }

    /**
     * Creates and then prints the last message after the match ends. Contains the name of the winning player
     * @param winner String that represents the name of the winner
     */
    @Override
    public void winnerMessage(String winner) {
        String message = "Congratulations to " + winner + " for the win!";
        CLIMessenger.printMessage(message);
    }

    /**
     * Creates and then prints a message representing the scoreboard
     * @param scoreboard String matrix that represents the scoreboard
     */
    @Override
    public void scoreboardMessage(String[][] scoreboard) {
        String message = "";
        int j;
        for(int i = 0; i < scoreboard.length; i++) {
            j = i + 1;
            message = message.concat(scoreboard[i][0] + " finished " + j + " with a total score of " + scoreboard[i][1] + "\n");
        }
        CLIMessenger.printMessage(message);
    }

    /**
     * Updates the model based on the received matchsnapshot
     * @param matchSnapshot Snapshot of the match containing all the info needed to represent the game state to the
     *                      client
     */
    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        CLIMessenger.updateView(matchSnapshot);
    }
}
