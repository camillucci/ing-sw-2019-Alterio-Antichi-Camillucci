package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.action.Action;

public class MatchManager {
    private Match match;
    private TurnManager turnManager;

    public MatchManager() {
        //TODO this.match = new Match(lobby.getPlayers(), lobby.getPlayerColors(), lobby.getGameLength(), lobby.getGameSize());
        this.turnManager = new TurnManager(match);
    }

    public void handleAction(Action action) {
        turnManager.handleAction(action);
    }

    public void calculateScore() {
        //TODO
    }

    public void declareWinner() {
        //TODO
    }
}
