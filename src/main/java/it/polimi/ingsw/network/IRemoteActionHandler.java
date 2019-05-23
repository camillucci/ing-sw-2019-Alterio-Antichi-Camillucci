package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.IEvent;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteActionHandler extends Remote
{
    IEvent<RemoteActionsHandler, List<RemoteAction>> getNewActionsEvent() throws IOException;
    void chooseAction(int index) throws RemoteException;
    void doAction() throws RemoteException;
    void addTargetPlayer(int index) throws RemoteException;
    void addTargetSquare(int index) throws RemoteException;
    void addPowerup(int index) throws RemoteException;
    void addDiscardablePowerup(int index) throws RemoteException;
    void addWeapon(int index) throws RemoteException;
    List<String> getPossiblePlayers() throws RemoteException;
    List<String> getPossibleSquares() throws RemoteException;
    List<String> getPossiblePowerups() throws RemoteException;
    List<String> getDiscardablePowerUps() throws RemoteException;
    boolean canBeDone() throws RemoteException;
}
