package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.network.Command;
import it.polimi.ingsw.network.IAdrenalineServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIAdrenalineServer extends Remote {
    void registerClient(ICallbackAdrenalineClient client) throws RemoteException;
    void ping() throws RemoteException;
    void newServerCommand(Command<IAdrenalineServer> command) throws RemoteException;
}
