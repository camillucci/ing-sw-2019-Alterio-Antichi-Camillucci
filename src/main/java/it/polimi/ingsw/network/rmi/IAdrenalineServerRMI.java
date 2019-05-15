package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.network.IAdrenalineServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAdrenalineServerRMI extends IAdrenalineServer, RMIConnection, Remote
{
    <T extends Remote> void connect(T client) throws RemoteException;
}
