package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteAdrenalineClient extends Remote {
    void newMessage(String message) throws RemoteException;
    void newMatchSnapshot(MatchSnapshot matchSnapshot) throws RemoteException;
}
