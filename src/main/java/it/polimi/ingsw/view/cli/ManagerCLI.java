package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.view.ViewInterface;

public class ManagerCLI implements ViewInterface {
    //TODO private Client client;
    private ParserCLI parser;
    private MessengerCLI massanger;
    private Match match;
    private String name;
    private boolean onTurn = false;

    public ManagerCLI() {
        parser = new ParserCLI();
        massanger = new MessengerCLI();
    }

    public void CLI(Match match) {
        this.match = match;
        parser = new ParserCLI();
        massanger = new MessengerCLI();
        //TODO
    }

    public void startConnection() {
        //TODO
    }

    private void login() {
        boolean check = false;
        String name = null;
        while(!check) {
            massanger.insertName();
            name = parser.parseName();
            //TODO add connection methods
        }
    }

    private void displayRollback() {
        while(onTurn) {
            massanger.displayRollback();
        }
    }

    public void setTurn(boolean turn) {
        onTurn = turn;
        return;
    }

    @Override
    public void updateMatch(Match match) {
        //TODO add other displayed features when user is current player on turn
        displayRollback();
    }

    @Override
    public void displayMessage(String message) {
        //TODO
    }


}
