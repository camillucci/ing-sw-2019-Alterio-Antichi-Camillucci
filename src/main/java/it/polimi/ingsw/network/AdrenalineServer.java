package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.model.snapshots.MatchSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

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
        boolean interfaceType = client.in().getBool();
        String name = client.in().getObject();
        while(controller.existName(name))
            client.out().sendBool(false); // name not accepted
        client.out().sendBool(true); // name accepted
        room = getAvailableRoom();
        client.out().sendObject((Serializable) room.getAvailableColors()); //Possible problems with serializable?
        int color = client.in().getObject(); //color chosen by user
        client.out().sendBool(room.addPlayer(color, name, this));
    }

    public void matchStart() throws Exception {
        client.out().sendBool(true);
    }

    public void sendTargets(ArrayList<String> targets) throws Exception {
        client.out().sendObject(targets);
    }

    public int spawn(ArrayList<String> powerupCards) throws Exception {
        client.out().sendObject(powerupCards);
        return client.in().getInt();
    }

    private Room getAvailableRoom() {
        return controller.getAvailableRoom();
    }

    public void updateView(MatchSnapshot matchSnapshot) throws Exception {
        client.out().sendObject(matchSnapshot);
    }
}
