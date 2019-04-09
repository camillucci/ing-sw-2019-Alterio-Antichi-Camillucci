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
            Player p = new Player(playersName.get(i), playerColors.get(i), gameBoard);
            p.deathEvent.addEventHandler((s,a)->this.deadPlayers.add(s));
            players.add(p);
        }
        gameBoard.setPlayers(players);
    }

    public void spawn(){
        for(int i = 0; i < deadPlayers.size(); i++) {
            // TODO
        }
    }

    public void assignPoints(){
        List<Double> tempCount = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0);
        for(int i = 0; i < deadPlayers.size(); i++) {
            List<PlayerColor> damage = deadPlayers.get(i).getDamage();
            int tempSkull = deadPlayers.get(i).getSkull();
            if(!deadPlayers.get(i).isFinalFrenzy())
                players.get(playerColors.indexOf(damage.get(0))).addPoints(1);
            for(int j = 0; j < damage.size(); j++)
                tempCount.set(playerColors.indexOf(damage.get(j)), tempCount.get(playerColors.indexOf(damage.get(j))) + 1 + Math.pow(2, 12.0 - j) / 10000);
            for(int j = 0; j < players.size() - 1 && Collections.max(tempCount) > 0.0; j++) {
                if(!deadPlayers.get(i).isFinalFrenzy())
                    players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(8 - tempSkull * 2 - j * 2, 1));
                else
                    players.get(tempCount.indexOf(Collections.max(tempCount))).addPoints(Math.max(2 - tempSkull * 2 - j * 2, 1));
                tempCount.set(tempCount.indexOf(Collections.max(tempCount)), 0.0);
            }
            List<PlayerColor> tempKillShot = new ArrayList<>();
            tempKillShot.add(damage.get(10));
            if(damage.size() == 12)
                tempKillShot.add(damage.get(11));
            gameBoard.addKillShotTrack(tempKillShot);
        }
    }

    public void addDeadPlayers(Player deadPlayer) {
        this.deadPlayers.add(deadPlayer);
    }

    public void rollback(List<Player> clonedPlayers) {
        players = clonedPlayers;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
