package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;

import java.io.IOException;
import java.util.List;

public abstract class ActionHandler extends ViewElement
{
    public final IEvent<ActionHandler, Command<RemoteActionsHandler>> newCommand = new Event<>();
    public final IEvent<ActionHandler, RemoteAction> actionDoneEvent = new Event<>();
    public abstract void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException;
    public abstract void updateActionData(RemoteAction.Data data) throws IOException;
    protected void notifyChoice(Command<RemoteActionsHandler> command)
    {
        ((Event<ActionHandler, Command<RemoteActionsHandler>>)newCommand).invoke(this, command);
    }
}
