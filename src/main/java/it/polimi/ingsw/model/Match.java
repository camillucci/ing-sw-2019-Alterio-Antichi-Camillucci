package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Match {

    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private ArrayList<Player> deadPlayers;
    private Turn currentTurn;


    public Match(ArrayList<String> playersName) {
        this.players = new ArrayList<Player>();
        for(String name: playersName)
            players.add(new Player(name));
        gameBoard = new GameBoard(players);
        deadPlayers = new ArrayList<Player>;
    }

    public void spawn(){
        // TODO
    }

    public void assignPoints(){
        // TODO
    }
}
