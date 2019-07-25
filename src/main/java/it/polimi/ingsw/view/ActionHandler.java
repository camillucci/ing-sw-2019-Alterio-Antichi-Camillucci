package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.view.gui.endgame.EndGameController;

import java.io.IOException;
import java.util.List;

/**
 * This is the interface that's dedicated to receiving all the possible actions available to current's turn user and
 * displaying them to said user, so they could choose which one they want to go for. It also notifies the server with
 * which action is selected by the user.
 */

public abstract class ActionHandler implements ViewElement {
    /**
     * Event other classes can subscribe to. It is used to notify the subscribed classes of when a
     * new command is chosen
     */
    public final IEvent<ActionHandler, Command<RemoteActionsHandler>> newCommand = new Event<>();
    public final IEvent<ActionHandler, EndGameController.EndGameData> matchEndedEvent = new Event<>();

    /**
     * Event other classes can subscribe to. It is used to notify the subscribed classes of when the chosen action is
     * completed
     */
    public final IEvent<ActionHandler, RemoteAction> actionDoneEvent = new Event<>();

    /**
     * Gets a list of possible actions and displays them to the user. Calls the other methods to make sure a
     * notification is provided to the server once a choice has been taken by the user.
     * @param options Is the list of actions available the user can choose from
     * @throws IOException IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public abstract void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException;

    /**
     * Method that describes what to do when the server notifies the client about the declaration of a winner. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param winner Name of the winner of the match
     */
    public abstract void winnerMessage(String winner);

    /**
     * Method that describes what to do when the server notifies the client about the reconnection of a player. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param name Name of the reconnected player
     */
    public abstract void reconnectedMessage(String name);

    /**
     * Method that describes what to do when the server sends the client the scoreboard relative to the match. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param scoreboard Matrix of Strings that represents the scoreboard relative to the match this client is playing
     *                   in
     */
    public abstract void scoreboardMessage(String[][] scoreboard);


    public abstract void updateActionData(RemoteAction.Data data) throws IOException;

    public void onTurnStart(){
        // nothing
    }
    protected void notifyChoice(Command<RemoteActionsHandler> command)
    {
        ((Event<ActionHandler, Command<RemoteActionsHandler>>)newCommand).invoke(this, command);
    }

}
