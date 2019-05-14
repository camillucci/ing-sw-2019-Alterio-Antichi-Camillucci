package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.BiConsumer;

public interface IAdrenalineServer extends IConnection, Remote
{
    void setInterface(boolean ClI) throws RemoteException;
    List<String> availableColors() throws IOException;
    void setColor(int colorIndex) throws RemoteException;
    boolean setName(String name) throws RemoteException;
    void setGameLength(int gameLength) throws RemoteException;
    void setGameMap(int choice) throws RemoteException;
    boolean joinRoom() throws IOException;
    void updateView(MatchSnapshot matchSnapshot) throws IOException;
    void handleAction(int selection, int extra) throws IOException;
    void subscribeNewMessageEvent(BiConsumer<IAdrenalineServer, String> eventHandler) throws IOException;
}
