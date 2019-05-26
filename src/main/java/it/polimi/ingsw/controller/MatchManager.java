package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;

public class MatchManager {
    private Match match;
    private Room room;

    public MatchManager(Match match, Room room) {
        this.match = match;
        this.room = room;
    }

    private void startMatch() {
        match.start();
        ArrayList<Action> actions = (ArrayList<Action>) match.getActions();
        int client = match.getPlayerIndex();
        //room.sendActions(actions, client);
    }

    /*
    public void handleAction(int index) throws Exception {
        match.getActions().get(index).doAction();
        for(int i = 0; i < room.getPlayerNames().size(); i++) {
            room.updateView(match.createSnapshot(i), i);
        }
        ArrayList<Action> actions = (ArrayList<Action>) match.getActions();
        int client = match.getPlayerIndex();
        //room.sendActions(actions, client);
    }

     */

    public String[][] calculateScore() {
        String[][] scoreBoard = new String[match.getPlayers().size()][2];
        for(int i = 0; i < match.getPlayers().size(); i++) {
            scoreBoard[i][0] = match.getPlayers().get(i).name;
            scoreBoard[i][1] = Integer.toString(match.getPlayers().get(i).getPoints());
        }
        for(int i = 0; i < match.getPlayers().size(); i++)
            for(int j = i; j < match.getPlayers().size(); j++)
                if(Integer.parseInt(scoreBoard[j][1]) > Integer.parseInt(scoreBoard[i][1])) {
                    String[] temp = new String[]{scoreBoard[i][0], scoreBoard[i][1]};
                    scoreBoard[i][0] = scoreBoard[j][0];
                    scoreBoard[i][1] = scoreBoard[j][1];
                    scoreBoard[j][0] = temp[0];
                    scoreBoard[j][1] = temp[1];
                }
        return scoreBoard;
    }

    public String declareWinner() {
        Player player = match.getPlayers().get(0);
        for(int i = 1; i < match.getPlayers().size(); i++)
            if(match.getPlayers().get(i).getPoints() > player.getPoints() || (match.getPlayers().get(i).getPoints() == player.getPoints() &&
                    countKillShotTrack(match.getPlayers().get(i)) > countKillShotTrack(player)))
                    player = match.getPlayers().get(i);
        return player.name;
    }

    private int countKillShotTrack(Player player) {
        int temp = 0;
        for(List<PlayerColor> list : match.gameBoard.getKillShotTrack())
            for(PlayerColor playerColor : list)
                if(playerColor == player.color)
                    temp++;
        return temp;
    }
}
