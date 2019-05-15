package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.AdrenalineServer;

public class AdrenalineServerRMI extends AdrenalineServer {
    private ICallbackAdrenalineClient client;

    public AdrenalineServerRMI(Controller controller) {
        super(controller);
    }

    public void initialize(ICallbackAdrenalineClient client)
    {
        this.client = client;
    }
}
