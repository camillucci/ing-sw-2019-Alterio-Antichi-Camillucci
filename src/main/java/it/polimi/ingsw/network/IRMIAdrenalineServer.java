package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIAdrenalineServer extends Remote {
    void registerClient(ICallbackAdrenalineClient client) throws RemoteException;
    void ping() throws RemoteException;
    void newServerCommand(Command<IAdrenalineServer> command) throws RemoteException;
}
