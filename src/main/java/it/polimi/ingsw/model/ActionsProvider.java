package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.model.action.Action;

import java.util.List;

public interface ActionsProvider
{
    Event<Player, List<Action>> newActionsEvent = new Event<>();
    Player getPlayer();
    List<Action> getActions();
}
