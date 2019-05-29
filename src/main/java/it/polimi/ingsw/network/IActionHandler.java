package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IActionHandler extends Remote {

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

