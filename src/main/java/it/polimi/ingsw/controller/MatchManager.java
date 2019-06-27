package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to provide a connection between the controller and the match that's being played.
 */
public class MatchManager {
    /**
     * reference to the match the class is managing
     */
    private Match match;

    /**
     * reference to the room relative to the match the class is managing
     */
    private Room room;

    /**
     * Constructor. It initializes the two parameter using the two inputs.
     * @param match reference to the match the class is managing
     * @param room reference to the room relative to the match the class is managing
     */
    public MatchManager(Match match, Room room) {
        this.match = match;
        match.endMatchEvent.addEventHandler((currentMatch, currentPlayers) -> { calculateScore(); declareWinner(); });
        // TODO Send Score and Winner to all Clients
        this.room = room;
    }

    /**
     * Starts the match (this method is called when the countdown reaches the end or when the room is full).
     * Gets all the possible actions the current player can choose to execute and gets a reference to the client
     * relative to current's player.
     */
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

    /**
     * This method is called when the match reaches an end and associates to every player a number indicative of the
     * score they got by playing the game. Then it creates a leader board based on the amount of points that every
     * player has acquired.
     * @return The leader board that represents the final ladder based on player points.
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

    /**
     * Calculates who's the winning player by finding the player with the maximum number of points
     * @return Winning player's name
     */
    public String declareWinner() {
        Player player = match.getPlayers().get(0);
        for(int i = 1; i < match.getPlayers().size(); i++)
            if(match.getPlayers().get(i).getPoints() > player.getPoints() || (match.getPlayers().get(i).getPoints() == player.getPoints() &&
                    countKillShotTrack(match.getPlayers().get(i)) > countKillShotTrack(player)))
                    player = match.getPlayers().get(i);
        return player.name;
    }

    /**
     * Calculates the kill shot Track associated with a specific player by confronting their color with color of all the
     * drops present on the game board killshot track.
     * @param player Player whose killshotTrack is going to be calculated.
     * @return The killshotTrack associated with the player.
     */
    private int countKillShotTrack(Player player) {
        int temp = 0;
        for(List<PlayerColor> list : match.getGameBoard().getKillShotTrack())
            for(PlayerColor playerColor : list)
                if(playerColor == player.color)
                    temp++;
        return temp;
    }
}
