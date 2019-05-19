package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;

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

    public void calculateScore() {
        //TODO
    }

    public void declareWinner() {
        //TODO
    }
}
