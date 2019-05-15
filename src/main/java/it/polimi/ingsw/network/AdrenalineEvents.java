package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.util.List;

public abstract class AdrenalineEvents
{
    public final IEvent<AdrenalineEvents, MatchSnapshot> modelChangedEvent = new Event<>();
    public final IEvent<AdrenalineEvents, MatchSnapshot> matchStartEvent = new Event<>();
    public final IEvent<AdrenalineEvents, String> newMessage = new Event<>();
    public final IEvent<AdrenalineEvents, List<RemoteAction>> newActionsEvent = new Event<>();
}
