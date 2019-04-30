package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.socket.TCPClient;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;

public class ManagerCLI implements ViewInterface {
    private Client client;
    private ParserCLI parser;
    private MessengerCLI messanger;
    private TCPClient tcp;
    private final String hostname = "1207.0.0.1";
    private final int ip = 10003;

    public ManagerCLI() {
        parser = new ParserCLI();
        messanger = new MessengerCLI();
    }

    public void ManagerCLI(Match match, Client client){
        parser = new ParserCLI();
        messanger = new MessengerCLI();
        this.client = client;
        //TODO
    }

    public void login() throws IOException{
        String name = null;
        while(name == null) {
            messanger.insertName();
            parser.parseName();
        }
        client.setName(name);
        messanger.askConnection();
        client.setConnection(parser.parseChoice());
        messanger.askInterface();
        client.setInterface(parser.parseChoice());
    }

    public void startConnection() throws IOException {
        this.tcp = TCPClient.connect(hostname, ip);
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
    }

    @Override
    public void displayMessage(String message) {
        //TODO
    }


}
