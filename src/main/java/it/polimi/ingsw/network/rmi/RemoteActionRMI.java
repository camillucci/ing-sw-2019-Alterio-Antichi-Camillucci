package it.polimi.ingsw.network.rmi;

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

    public void initialize(IRemoteActionHandler remoteActionsHandler) throws RemoteException
    {
        this.remoteActionsHandler = remoteActionsHandler;
        remoteActionsHandler.chooseAction(this.index);
    }

    @Override
    public void addTargetPlayer(String target) throws RemoteException
    {
        remoteActionsHandler.addTargetPlayer(possiblePlayers.indexOf(target));
    }

    @Override
    public void addTargetSquare(String target) throws RemoteException
    {
        remoteActionsHandler.addTargetSquare(possiblePlayers.indexOf(target));
    }

    @Override
    public void usePowerUp(String powerUp) throws IOException {
        remoteActionsHandler.addPowerup(possiblePowerUps.indexOf(powerUp));
    }

    @Override
    public void addDiscardable(String powerUp) throws IOException {
        remoteActionsHandler.addDiscardablePowerup(discardablePowerUps.indexOf(powerUp));
    }

    @Override
    public void addWeapon(String weapon) throws IOException {
        remoteActionsHandler.addWeapon(ownerPlayer.getUnloadedWeapons().indexOf(weapon));
    }

    @Override
    public void addDiscardableAmmo(String ammo) throws IOException {
        //TODO
    }

    @Override
    public void doAction() throws RemoteException
    {
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
        List<String> ret = remoteActionsHandler.getPossiblePowerups();
        this.possiblePowerUps = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public List<String> getDiscardablePowerups() throws IOException {
        List<String> ret = remoteActionsHandler.getDiscardablePowerUps();
        this.discardablePowerUps = new ArrayList<>(ret);
        return ret;
    }

    @Override
    public List<String> getDiscardableAmmos() throws IOException, ClassNotFoundException {
        //TODO
        return null;
    }

    @Override
    public List<String> getPossibleWeapons() throws IOException, ClassNotFoundException {
        //TODO
        return null;
    }

    @Override
    public boolean canBeDone() throws RemoteException
    {
        return remoteActionsHandler.canBeDone();
    }

    @Override
    public String toString() {
        return "Azione";
    }
}
