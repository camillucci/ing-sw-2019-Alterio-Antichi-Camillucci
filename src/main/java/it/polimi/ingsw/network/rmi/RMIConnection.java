package it.polimi.ingsw.network.rmi;

import java.io.IOException;
import java.rmi.Remote;

public interface RMIConnection  <T extends Remote, V extends Remote>  extends Remote
{
    T connect(V client) throws IOException;
}
