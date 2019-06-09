package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.view.View;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICallbackAdrenalineClient extends Remote {
    void newViewCommand(Command<View> command) throws RemoteException;
    void ping() throws RemoteException;
}
