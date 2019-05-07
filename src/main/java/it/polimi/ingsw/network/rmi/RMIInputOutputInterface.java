package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.InputInterface;
import it.polimi.ingsw.generics.InputStreamUtils;
import it.polimi.ingsw.generics.OutputInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIInputOutputInterface extends InputInterface, OutputInterface, Remote
{
    void connect() throws RemoteException;
    boolean isConnected() throws RemoteException;

}

