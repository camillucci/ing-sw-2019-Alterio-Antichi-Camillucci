package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;
import it.polimi.ingsw.network.RemoteAction;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ICallbackAdrenalineClient extends Remote {
    void newMessage(String message) throws RemoteException;
    void modelChanged(MatchSnapshot matchSnapshot) throws RemoteException;
    void matchStart(MatchSnapshot matchSnapshot) throws RemoteException;
    void newActions(List<RemoteAction> newActions) throws RemoteException;
}
