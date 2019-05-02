package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;

import java.io.Serializable;

public class AdrenalineServer
{
    private Controller controller;
    private Client client;
    private Room room;

    protected AdrenalineServer(Controller controller, Client client)
    {
        this.controller = controller;
        this.client = client;
    }

    private void login() throws Exception
    {
        boolean interfaeType = client.in().getBool();
        while(controller.existName(client.in().getObject()))
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
        room = getAvailableRoom();
        client.out().sendObject((Serializable) room.getPlayerColors());
    }

    private Room getAvailableRoom() {
        //TODO
        return null;
    }
}
