package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.util.List;

public abstract class ActionHandler extends ViewElement
{
    public final IEvent<ActionHandler, RemoteAction> choiceEvent = new Event<>();
    public final IEvent<ActionHandler, RemoteAction> actionDoneEvent = new Event<>();
    public abstract void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException;
}
