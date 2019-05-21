package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.socket.RemoteActionSocket;

import java.io.IOException;
import java.util.List;

public abstract class ActionHandler extends ViewElement
{
    public final IEvent<ActionHandler, Integer> choiceEvent = new Event<>();

    public abstract void chooseAction(List<RemoteAction> options) throws IOException, ClassNotFoundException;
}
