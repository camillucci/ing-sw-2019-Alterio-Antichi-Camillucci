package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.RemoteAction;

public interface RemoteActionsProvider
{
    RemoteAction getAction();
    IEvent<RemoteActionsProvider, RemoteAction> newActionsEvent();
    IEvent<RemoteActionsProvider, Object> notifyingToServeEvent();
}
