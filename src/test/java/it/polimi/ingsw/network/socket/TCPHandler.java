package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.network.Server;

import java.io.IOException;

public class TCPHandler {
    TCPClient client;
    Server server;

    public TCPHandler (TCPClient client) {
        this.client = client;
    }

    public void getUsername () throws IOException, ClassNotFoundException {
        client.in.getObject();
        Room tempRoom = server.getJoinableRoom();
    }
}
