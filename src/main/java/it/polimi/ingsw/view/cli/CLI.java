package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.view.ViewInterface;

public class CLI implements ViewInterface {
    //TODO private Client client;
    private ParserCLI parser;
    private MassengerCLI massanger;
    private Match match;

    public CLI() {
        parser = new ParserCLI();
        massanger = new MassengerCLI();
    }

    public void startConnection() {
        //TODO
    }

    private void login() {
        boolean check = false;
        String name = "";
        while(!check) {
            massanger.insertName();
            name = parser.parseName();
            //TODO add connection methods
        }
    }


    @Override
    public void updateMatch(Match match) {
        //TODO
    }

    @Override
    public void displayMessage(String message) {
        //TODO
    }


}
