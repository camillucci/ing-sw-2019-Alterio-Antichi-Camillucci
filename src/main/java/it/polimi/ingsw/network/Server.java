package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.network.socket.TCPListener;

import java.util.ArrayList;
import java.util.List;

public class Server {
    List<Room> lobby = new ArrayList<>();
    List<Client> clients = new ArrayList<>();
    private static final int IP = 10000;
    TCPListener tcpListener = new TCPListener(IP);


    public List<Room> getMatches() {
        //TODO;
        return null;
    }

    public Room getJoinableRoom() {
        //TODO
        return null;
    }
}
