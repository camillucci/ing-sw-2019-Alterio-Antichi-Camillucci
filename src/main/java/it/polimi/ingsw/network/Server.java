package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.socket.TCPListener;

import java.util.ArrayList;

public class Server {
    ArrayList<Match> matchList = new ArrayList<Match>();
    private final int ip = 10000;
    TCPListener tcpListener = new TCPListener(ip);


    public ArrayList<Match> getMatches() {
        return matchList;
    }


}
