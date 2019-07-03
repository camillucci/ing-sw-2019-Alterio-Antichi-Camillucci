package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;

import java.io.IOException;
import java.util.List;

/**
 * This is the interface that's dedicated to receiving all the possible actions available to current's turn user and
 * displaying them to said user, so they could choose which one they want to go for. It also notifies the server with
 * which action is selected by the user.
 */

public abstract class ActionHandler extends ViewElement
{
    /**
     * Event other classes can subscribe to. It is used to notify the subscribed classes of when a
     * new command is chosen
     */
    public final IEvent<ActionHandler, Command<RemoteActionsHandler>> newCommand = new Event<>();

    /**
     * Event other classes can subscribe to. It is used to notify the subscribed classes of when the chosen action is
     * completed
     */
    public final IEvent<ActionHandler, RemoteAction> actionDoneEvent = new Event<>();

    /**
     * Gets a list of possible actions and displays them to the user. Calls the other methods to make sure a
     * notification is provided to the server once a choice has been taken by the user.
     * @param options Is the list of actions available the user can choose from
     */
    public abstract void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException;
    public abstract void updateActionData(RemoteAction.Data data) throws IOException;

    public void onTurnStart(){
        // nothing
    }
    protected void notifyChoice(Command<RemoteActionsHandler> command)
    {
        ((Event<ActionHandler, Command<RemoteActionsHandler>>)newCommand).invoke(this, command);
    }

}
