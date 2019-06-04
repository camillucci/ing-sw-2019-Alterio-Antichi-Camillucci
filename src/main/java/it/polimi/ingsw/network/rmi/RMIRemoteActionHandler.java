package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RMIRemoteActionHandler extends RemoteActionsHandler
{
    public RMIRemoteActionHandler(ActionsProvider provider, Player player)
    {
        super(player);
    }

    @Override
    protected List<RemoteAction> createRemoteActions(List<Action> actions)
    {
        List<RemoteAction> ret = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            ret.add(new RemoteActionRMI(i, actions.get(i).getText()));
        return ret;
    }

    @Override
    public void waitForClient() {
        // nothing
    }
}
