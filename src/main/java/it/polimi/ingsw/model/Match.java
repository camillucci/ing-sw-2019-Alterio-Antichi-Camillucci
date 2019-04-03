package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Match implements PlayerDeathSubscriber{

    private GameBoard gameBoard;
    private List<Player> players = new ArrayList<>();
    private List<Player> deadPlayers = new ArrayList<>();
    private Turn currentTurn;
    private int gameLength;
    private int gameSize;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize) {

        this.gameLength = gameLength;
        this.gameSize = gameSize;
        this.gameBoard = new GameBoard(gameLength, gameSize);
        for(int i = 0; i < playersName.size(); i++) {
            players.add(new Player(playersName.get(i), playerColors.get(i), gameBoard));
        }
        gameBoard.setPlayers(players);
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
