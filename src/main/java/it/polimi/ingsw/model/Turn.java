package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.*;

import java.util.ArrayList;

public class Turn {

    private static int frenzyCounter = 0;
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
        this.branchMap.endOfBranchMapReachedEvent.addEventHandler((s,e)->onMoveTerminated());
        this.branchMap.rollbackEvent.addEventHandler((s,e)->rollback());
    }

    private void onMoveTerminated()
    {
        moveCounter--;
        if(moveCounter == 0) {
            //TODO Notify Match
        }
        else
            createBranchMap();
    }

    private void createBranchMap(){
        if(match.getFinalFrenzy()){
            if(frenzyCounter <= match.getPlayers().size() - match.getFrenzyStarter()) {
                this.branchMap = BranchMapFactory.adrenalineX2();
            }
            else {
                this.branchMap = BranchMapFactory.adrenalineX1();
            }

            frenzyCounter++;
        }
        else {
            if (currentPlayer.getDamage().size() >= 3) {
                if (currentPlayer.getDamage().size() >= 6) {
                    this.branchMap = BranchMapFactory.sixDamage();
                } else {
                    this.branchMap = BranchMapFactory.threeDamage();
                }
            } else {
                this.branchMap = BranchMapFactory.noAdrenaline();
            }
        }
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
