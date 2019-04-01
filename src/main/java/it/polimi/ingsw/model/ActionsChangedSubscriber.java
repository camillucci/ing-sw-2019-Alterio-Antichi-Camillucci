package it.polimi.ingsw.model;

import java.util.List;

public interface ActionsChangedSubscriber
{
    public void onActionsChanged(List<Action> actions);
}
