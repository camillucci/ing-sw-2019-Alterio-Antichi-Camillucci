package it.polimi.ingsw.model;

public class Turn {

    private int turnCounter;
    private Player currentPlayer;
    private int moveCounter;
    private BranchMap branchMap;


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
}
