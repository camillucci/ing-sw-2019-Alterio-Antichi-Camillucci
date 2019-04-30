package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.socket.TCPListener;

import java.util.ArrayList;

public class Server {
    ArrayList<Room> lobby = new ArrayList<Room>();
    ArrayList<Client> clients = new ArrayList<Client>();
    private final int ip = 10000;
    TCPListener tcpListener = new TCPListener(ip);


    public ArrayList<Room> getMatches() {
        //TODO;
        return null;
    }

    public Room getJoinableRoom() {

        return null;
    }
}
