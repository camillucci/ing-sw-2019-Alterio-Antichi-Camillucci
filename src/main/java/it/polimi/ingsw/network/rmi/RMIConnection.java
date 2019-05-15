package it.polimi.ingsw.network.rmi;

import java.io.IOException;
import java.rmi.Remote;

public interface RMIConnection extends Remote
{
    <V extends Remote> void connect(V client) throws IOException;
}
