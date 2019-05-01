package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;

public class ManagerCLI implements ViewInterface {
    private ParserCLI parser;
    private MessengerCLI messanger;

    public ManagerCLI() {
        parser = new ParserCLI();
        messanger = new MessengerCLI();
    }

    public void ManagerCLI(Client client){
        parser = new ParserCLI();
        messanger = new MessengerCLI();
        //TODO
    }

    public void login() throws IOException {
        messanger.askConnection();
        boolean connectionType = parser.parseChoice();
        messanger.askInterface();
        boolean interfaceType = parser.parseChoice();
        //this.client = new Client(connectionType, interfaceType);
        String name = null;
        while(name == null) {
            messanger.insertName();
            parser.parseName();
        }
        //client.setName(name);
    }

    private void displayRollback() {
       /*
        while(client.getOnTurn()) {
            messanger.displayRollback();
        }
        */
    }

    public void setTurn(boolean turn) {
        //client.setOnTurn(turn);
        return;
    }

    @Override
    public void updateMatch(Match match) {
        //TODO add other displayed features when user is current player on turn
    }

    @Override
    public void displayMessage(String message) {
        //TODO
    }


}
