package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConnection extends Remote
{
    void connect(Runnable func) throws RemoteException;
}
