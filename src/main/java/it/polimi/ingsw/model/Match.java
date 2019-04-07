package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Match {

    private GameBoard gameBoard;
    private List<Player> players = new ArrayList<>();
    private List<Player> deadPlayers = new ArrayList<>();
    private List<PlayerColor> playerColors;
    private Turn currentTurn;
    private boolean finalFrenzy;
    private int gameLength;
    private int gameSize;

    public Match(List<String> playersName, List<PlayerColor> playerColors, int gameLength, int gameSize) {

        this.gameLength = gameLength;
        this.gameSize = gameSize;
        this.finalFrenzy = false;
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
        List<Integer> tempCount = Arrays.asList(0, 0, 0, 0, 0);
        for(int i = 0; i < deadPlayers.size(); i++) {
            List<PlayerColor> damage = deadPlayers.get(i).getDamage();
            int tempSkull = deadPlayers.get(i).getSkull();
            if(!finalFrenzy)
                players.get(playerColors.indexOf(damage.get(0))).addPoints(1);
            for(int j = 0; j < damage.size(); j++)
                tempCount.set(playerColors.indexOf(damage.get(j)), tempCount.get(playerColors.indexOf(damage.get(j)))+1);
            for(int j = 0; j < players.size() + deadPlayers.size() - 1; j++) {
                if(!finalFrenzy)
                    players.get(Collections.max(tempCount)).addPoints(Math.max(8 - tempSkull * 2 - j * 2, 1));
                else
                    players.get(Collections.max(tempCount)).addPoints(Math.max(2 - tempSkull * 2 - j * 2, 1));
                tempCount.set(Collections.max(tempCount), 0);
            }
        }
        //TODO Assegnare i punti sulla Killshot Track
    }
}
