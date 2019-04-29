package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.Socket.TCPClient;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;

public class MessengerCLI {
    Client client;

    public void insertName() {
        TCPClient tcp = client.getTCP();
        tcp.displayString("Insert username here");
    }

    public void displayRollback() {
        //TODO
    }

    public void displayOptions(ArrayList<Player> players, ArrayList<Square> squares) {
        //TODO
    }
}
