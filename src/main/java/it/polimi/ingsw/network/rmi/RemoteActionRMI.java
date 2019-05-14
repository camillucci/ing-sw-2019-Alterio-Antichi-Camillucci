package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;
import it.polimi.ingsw.network.IRemoteActionHandler;
import it.polimi.ingsw.network.RemoteAction;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RemoteActionRMI extends RemoteAction
{
    private IRemoteActionHandler remoteActionsHandler;

    public RemoteActionRMI(int index){
        super(index);
    }

    public void inizialize(IRemoteActionHandler remoteActionsHandler) throws RemoteException
    {
        this.remoteActionsHandler = remoteActionsHandler;
        remoteActionsHandler.chooseAction(this.index);
    }

    @Override
    public void addTargetPlayer(String target) throws RemoteException
    {
        int i = possiblePlayers.indexOf(target);
        if(i != -1)
            remoteActionsHandler.addTargetPlayer(i);
    }

    @Override
    public void addTargetSquare(String target) throws RemoteException
    {
        //server.handleAction(RemoteActionsHandlerSocket.ADD_SQUARE, possibleSquares.indexOf(target));

        int i = possibleSquares.indexOf(target);
        if(i != -1)
            remoteActionsHandler.addTargetSquare(i);
    }

    @Override
    public void usePowerUp(String powerUp) throws IOException {

    }

    @Override
    public void addDiscardable(String powerUp) throws IOException {

    }

    @Override
    public void addWeapon(String weapon) throws IOException {

    }

    @Override
    public void doAction() throws RemoteException
    {
        //server.handleAction(RemoteActionsHandlerSocket.DO_ACTION, 0);
        remoteActionsHandler.doAction();
    }

    @Override
    public List<String> getPossiblePlayers() throws RemoteException
    {
        List<String> ret = remoteActionsHandler.getPossiblePlayers();
        this.possiblePlayers = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public List<String> getPossibleSquares() throws RemoteException
    {
        List<String> ret = remoteActionsHandler.getPossibleSquares();
        this.possibleSquares = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public List<String> getPossiblePowerups() throws IOException {
        return null;
    }

    @Override
    public List<String> getDiscardablePowerups() throws IOException {
        return null;
    }

    @Override
    public boolean canBeDone() throws RemoteException
    {
        return remoteActionsHandler.canBeDone();
    }
}
