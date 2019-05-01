package it.polimi.ingsw.network;

public interface AckInterface
{
    public void sendAck(boolean ack);
    public boolean getAck();
}
