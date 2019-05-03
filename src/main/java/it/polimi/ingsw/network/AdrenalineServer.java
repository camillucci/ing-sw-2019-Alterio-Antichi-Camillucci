package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.model.PlayerColor;

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
        String name = client.in().getObject();
        while(controller.existName(name))
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
        room = getAvailableRoom();
        client.out().sendObject((Serializable) room.getPlayerColors()); //Possible problems with serializable?
        PlayerColor color = client.in().getObject(); //color chosen by user
        client.out().sendBool(room.addPlayer(color, name));
    }

    public void matchStart() throws Exception {
        client.out().sendBool(true);
    }

    private Room getAvailableRoom() {
        return controller.getAvailableRoom();
    }
}
