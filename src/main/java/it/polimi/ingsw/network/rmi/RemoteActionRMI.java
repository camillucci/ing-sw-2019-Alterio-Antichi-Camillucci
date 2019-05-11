package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.IRemoteActionsHandler;
import it.polimi.ingsw.network.RemoteAction;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteActionRMI extends RemoteAction
{
    private IRemoteActionsHandler remoteActionsHandler;

    public RemoteActionRMI(int index){
        super(index);
    }

    public void inizialize(IRemoteActionsHandler remoteActionsHandler) throws RemoteException
    {
        this.remoteActionsHandler = remoteActionsHandler;
        remoteActionsHandler.chooseAction(this.index);
    }

    @Override
    public void addTarget(PublicPlayerSnapshot target) throws RemoteException
    {
        int i = possiblePlayers.indexOf(target);
        if(i != -1)
            remoteActionsHandler.addTargetPlayer(i);
    }

    @Override
    public void addTarget(SquareSnapshot target) throws RemoteException
    {
        //server.handleAction(RemoteActionsHandlerSocket.ADD_SQUARE, possibleSquares.indexOf(target));

        int i = possibleSquares.indexOf(target);
        if(i != -1)
            remoteActionsHandler.addTargetSquare(i);
    }

    @Override
    public void doAction() throws RemoteException
    {
        //server.handleAction(RemoteActionsHandlerSocket.DO_ACTION, 0);
        remoteActionsHandler.doAction();
    }

    @Override
    public List<PublicPlayerSnapshot> getPossiblePlayers() throws RemoteException
    {
        List<PublicPlayerSnapshot> ret = remoteActionsHandler.getPossiblePlayers();
        this.possiblePlayers = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public List<SquareSnapshot> getPossibleSquares() throws RemoteException
    {
        List<SquareSnapshot> ret = remoteActionsHandler.getPossibleSquares();
        this.possibleSquares = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public boolean canBeDone() throws RemoteException
    {
        return remoteActionsHandler.canBeDone();
    }
}
