package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.BranchMap;

import java.util.ArrayList;

public class Turn {

    private int turnCounter;
    private Player currentPlayer;
    private int moveCounter;
    private BranchMap branchMap;
    private Match match;
    private ArrayList<Player> clonedPlayers;


    public Turn(int turnCounter, Player currentPlayer) {
        this.turnCounter = turnCounter;
        this.currentPlayer = currentPlayer;
        this.moveCounter = 2;
    }

    public void createBranchMap(){
        //TODO
    }

    public void rollback(){
        //TODO
    }

    private void clonePlayers() {
        for (Player p : match.getPlayers()) {
            clonedPlayers.add(p.getClone());
        }
    }
}
