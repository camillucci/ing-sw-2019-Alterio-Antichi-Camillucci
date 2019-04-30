package it.polimi.ingsw.network;

import it.polimi.ingsw.network.socket.TCPClient;

import java.io.IOException;

public class Client {
    private String name;
    private boolean onTurn = false;
    private boolean connectionType;
    private boolean interfaceType;
    private TCPClient tcpClient;


    public Client(boolean connectionType, boolean interfaceType) throws IOException
    {
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;

        tcpClient = TCPClient.connect("127.0.0.1", 10000);
    }

    public boolean getOnTurn() {
        return onTurn;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
        return;
    }

    public void setName(String name) throws IOException {
        this.name = name;
        tcpClient.out.sendObject(name);
    }

    public void setConnection(boolean connectionType) {
        this.connectionType = connectionType;
    }

    public void setInterface(boolean interfaceType) {
        this.interfaceType = interfaceType;
    }
}
