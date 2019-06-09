package it.polimi.ingsw.network;

import it.polimi.ingsw.view.ActionHandler;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAdrenalineServer extends Remote
{
    List<String> availableColors();
    void setColor(int colorIndex) throws IOException;
    void setName(String name) throws IOException;
    void setGameLength(int gameLength);
    void setGameMap(int choice);
    void ready();
    void newActionCommand(Command<RemoteActionsHandler> command);
    void newServerCommand(Command<IAdrenalineServer> command);
}
