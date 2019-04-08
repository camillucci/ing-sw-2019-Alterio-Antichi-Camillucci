package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.branch.BranchMap;

public class Turn {

    private int turnCounter;
    private Player currentPlayer;
    private int moveCounter;
    private BranchMap branchMap;


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
        //TODO
    }

    private void clonePlayers() {

        //TODO
    }
}
