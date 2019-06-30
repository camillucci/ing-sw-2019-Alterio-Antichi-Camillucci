package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.network.RemoteAction;

import java.util.List;

public interface RemoteActionsProvider
{
    RemoteAction getAction();
    IEvent<RemoteActionsProvider, RemoteAction> newActionsEvent();
}
