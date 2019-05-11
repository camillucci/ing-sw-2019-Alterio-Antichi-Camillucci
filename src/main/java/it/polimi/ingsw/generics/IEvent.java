package it.polimi.ingsw.generics;

import java.rmi.Remote;
import java.util.function.BiConsumer;

public interface IEvent<T,U> extends Remote
{
    void addEventHandler(BiConsumer<T, U> eventHandler);
    void removeEventHandler(BiConsumer<T, U> eventHandler);
}