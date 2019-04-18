package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.action.Action;

public class Controller {
    MatchManager matchManager;

    public Controller() {
        this.matchManager = new MatchManager();
    }

    public void handleAction(Action action) {
        matchManager.handleAction(action);
        //TODO update all players view
    }
}
