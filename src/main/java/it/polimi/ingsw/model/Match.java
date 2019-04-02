package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Match implements PlayerDeathSubscriber{

    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private ArrayList<Player> deadPlayers;
    private Turn currentTurn;


    public Match(List<String> playersName, List<PlayerColor> playerColors) {
        this.players = new ArrayList<>();
        for(int i = 0; i < playersName.size(); i++) {
            players.add(new Player(playersName.get(i), playerColors.get(i)));
        }
        gameBoard = new GameBoard(players);
        deadPlayers = new ArrayList<>();
    }

    public void spawn(){
        // TODO
    }

    public void assignPoints(){
        // TODO
    }
}
