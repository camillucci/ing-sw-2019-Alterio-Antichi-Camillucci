package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.view.ViewInterface;

public class ManagerCLI implements ViewInterface {
    private Client client;
    private ParserCLI parser;
    private MessengerCLI messanger;
    private Match match;

    public ManagerCLI() {
        parser = new ParserCLI();
        messanger = new MessengerCLI();
    }

    public void ManagerCLI(Match match, Client client) {
        this.match = match;
        parser = new ParserCLI();
        messanger = new MessengerCLI();
        this.client = client;
        //TODO
    }

    private void login() {
        messanger.insertName();
    }

    private void displayRollback() {
        while(client.getOnTurn()) {
            messanger.displayRollback();
        }
    }

    public void setTurn(boolean turn) {
        client.setOnTurn(turn);
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
