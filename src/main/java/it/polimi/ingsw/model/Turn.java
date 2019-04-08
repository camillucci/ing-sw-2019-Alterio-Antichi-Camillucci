package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;
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

        createBranchMap();
    }

    private void eventsSetup()
    {
        this.branchMap.endOfBranchMapReachedEvent.addEventHandler((s,e)->createBranchMap());
        this.branchMap.rollbackEvent.addEventHandler((s,e)->rollback());
    }

    private void createBranchMap(){
        //TODO
        //this.branchMap = ..
        eventsSetup();
    }

    public void rollback(){
        moveCounter = moveCounter + 1;
        match.rollback(clonedPlayers);
        createBranchMap();
    }

    private void clonePlayers() {
        for (Player p : match.getPlayers()) {
            clonedPlayers.add(p.getClone());
        }
    }
}
