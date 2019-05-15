package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.IEvent;

import java.rmi.Remote;

public interface NotifiedConnection <V extends Remote>
{
    IEvent<NotifiedConnection, V> connectedEvent();
    IEvent<NotifiedConnection, V> disconnectedEvent();
}
