package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.ICallbackAdrenalineClient;

public interface IRMIAdrenalineServer extends IAdrenalineServer {
    void registerClient(ICallbackAdrenalineClient client);
    }
