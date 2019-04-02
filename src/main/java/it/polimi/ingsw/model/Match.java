package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Match implements PlayerDeathSubscriber{

    private GameBoard gameBoard;
    private List<Player> players;
    private List<Player> deadPlayers;
    private Turn currentTurn;
    private int gameLength;
    private int gameSize;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize) {
        this.players = new ArrayList<>();
        for(int i = 0; i < playersName.size(); i++) {
            players.add(new Player(playersName.get(i), playerColors.get(i)));
        }
        this.gameLength = gameLength;
        this.gameSize = gameSize;
        gameBoard = new GameBoard(players, gameLength, gameSize);
        deadPlayers = new ArrayList<>();
        this.gameLength = gameLength;
        this.gameSize = gameSize;
    }

    public void spawn(){
        for(int i = 0; i < deadPlayers.size(); i++) {
            // TODO
        }
    }

    public void assignPoints(){
        for(int i = 0; i < deadPlayers.size(); i++) {
            // TODO
        }
    }

    @Override
    public void onPlayerDeath(Player p) {
        //TODO
    }
}
