package it.polimi.ingsw.network.socket;

import java.io.Serializable;

public class Ack implements Serializable
{
    public final boolean ack;
    public Ack(boolean ack)
    {
        this.ack = ack;
    }
}
