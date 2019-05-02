package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Room;
import it.polimi.ingsw.generics.*;
import it.polimi.ingsw.network.socket.TCPClient;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

public abstract class Client // same for Socket and RMI
{
    public final Event<TCPClient, Object> disconnectedEvent = new Event<>();
    public abstract InputInterface in() throws Exception;
    public abstract OutputInterface out() throws Exception;
    public abstract void close();
    /*
    private String name;
    private boolean onTurn = false;
    private boolean connectionType;
    private boolean interfaceType;
    private TCPClient tcpClient;

    public boolean getOnTurn() {
        return onTurn;
    }

    public void setOnTurn(boolean onTurn) {
        this.onTurn = onTurn;
        return;
    }

    public abstract boolean setName(String name) throws Exception;
    */

}
