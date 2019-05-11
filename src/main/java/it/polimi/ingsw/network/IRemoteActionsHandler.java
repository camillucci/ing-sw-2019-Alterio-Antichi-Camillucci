package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.PublicPlayerSnapshot;
import it.polimi.ingsw.model.snapshots.SquareSnapshot;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IRemoteActionsHandler extends Remote
{
    IEvent<RemoteActionsHandler, List<RemoteAction>> getNewActionsEvent() throws RemoteException;
    void chooseAction(int index) throws RemoteException;
    void doAction() throws RemoteException;
    void addTargetPlayer(int index) throws RemoteException;
    void addTargetSquare(int index) throws RemoteException;
    List<PublicPlayerSnapshot> getPossiblePlayers() throws RemoteException;
    List<SquareSnapshot> getPossibleSquares() throws RemoteException;
    boolean canBeDone() throws RemoteException;
}
