package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.action.Action;

import java.util.List;

public abstract class ActionsProvider
{
    public final IEvent<Player, List<Action>> newActionsEvent = new Event<>();
    public abstract Player getPlayer();
    public abstract List<Action> getActions();
}
