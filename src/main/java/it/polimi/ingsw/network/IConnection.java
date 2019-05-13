package it.polimi.ingsw.network;

public interface IConnection
{
    void connect(Runnable func) throws Exception;
}
