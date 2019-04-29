package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.Socket.TCPClient;

public class Client {
    private String name;
    private boolean onTurn = false;
    private TCPClient tcp;

    public Client(TCPClient tcp){
        this.tcp = tcp;
    }

    public boolean getOnTurn() {
        return onTurn;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
        return;
    }

    public TCPClient getTCP() {
        return tcp;
    }

}
