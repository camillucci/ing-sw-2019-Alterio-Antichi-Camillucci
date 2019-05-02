package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;

public class AdrenalineServer
{
    private Controller controller;
    private Client client;

    protected AdrenalineServer(Controller controller, Client client)
    {
        this.controller = controller;
        this.client = client;
    }

    private void login() throws Exception
    {
        while(controller.existName(client.in().getObject()))
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
    }
}
