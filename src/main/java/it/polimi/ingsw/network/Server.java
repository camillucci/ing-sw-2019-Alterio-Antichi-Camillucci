package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Match;

import java.util.ArrayList;

public class Server {
    ArrayList<Match> matchList = new ArrayList<Match>();

    public ArrayList<Match> getMatches() {
        return matchList;
    }

}
