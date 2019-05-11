package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.AdrenalineServer;
import it.polimi.ingsw.network.RMIRemoteActionsHandler;

public class AdrenalineServerRMI extends AdrenalineServer {
    private RMIRemoteActionsHandler remoteActionsHandler;

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
    }
}
