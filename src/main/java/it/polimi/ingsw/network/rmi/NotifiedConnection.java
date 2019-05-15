package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.generics.IEvent;

import java.rmi.Remote;

public interface NotifiedConnection
{
    IEvent<NotifiedConnection, Remote> connectedEvent();
    IEvent<NotifiedConnection, Remote> disconnectedEvent();
}
