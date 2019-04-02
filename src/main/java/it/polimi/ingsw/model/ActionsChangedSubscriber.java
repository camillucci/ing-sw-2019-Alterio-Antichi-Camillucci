package it.polimi.ingsw.model;

import java.util.List;

public interface ActionsChangedSubscriber
{
    void onActionsChanged(List<Action> actions);
}
