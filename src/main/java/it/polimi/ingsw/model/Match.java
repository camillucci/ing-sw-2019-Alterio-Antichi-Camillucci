package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Match {

    private GameBoard gameBoard;
    private List<Player> players = new ArrayList<>();
    private List<Player> deadPlayers = new ArrayList<>();
    private List<PlayerColor> playerColors;
    private Turn currentTurn;
    private int gameLength;
    private int gameSize;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize) {

        this.gameLength = gameLength;
        this.gameSize = gameSize;
        this.playerColors = playerColors;
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
        List<Integer> temp = Arrays.asList(0, 0, 0, 0, 0);
        for(int i = 0; i < deadPlayers.size(); i++) {
            List<PlayerColor> damage = deadPlayers.get(i).getDamage();
            players.get(playerColors.indexOf(damage.get(0))).addPoints(1);
            for(int j = 0; j < damage.size(); j++) {
                temp.set(playerColors.indexOf(damage.get(j)), temp.get(playerColors.indexOf(damage.get(j)))+1);
            }
            //TODO To finish
        }
    }

}
