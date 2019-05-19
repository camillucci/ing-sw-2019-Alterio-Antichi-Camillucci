package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.ActionsProvider;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.network.RemoteAction;
import it.polimi.ingsw.network.RemoteActionsHandler;
import it.polimi.ingsw.network.rmi.RMIListener;
import it.polimi.ingsw.network.rmi.RemoteActionRMI;

import java.util.ArrayList;
import java.util.List;

public class RMIRemoteActionHandler extends RemoteActionsHandler
{
    private RMIListener listener;

    public RMIRemoteActionHandler(RMIListener listener, ActionsProvider provider, Player player)
    {
        super(provider, player);
        this.listener = listener;
        listener.export(this);
    }

    @Override
    protected List<RemoteAction> createRemoteActions(List<Action> actions)
    {
        List<RemoteAction> ret = new ArrayList<>();
        for(int i=0; i < actions.size(); i++)
            ret.add(new RemoteActionRMI(i));
        return ret;
    }
}
