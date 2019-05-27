package it.polimi.ingsw.network;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAdrenalineServer extends Remote
{
    List<String> availableColors() throws IOException;
    boolean setColor(int colorIndex) throws IOException;
    boolean setName(String name) throws RemoteException;
    void setGameLength(int gameLength) throws RemoteException;
    void setGameMap(int choice) throws RemoteException;
    boolean isHost() throws IOException;
    void ready() throws RemoteException;
}
