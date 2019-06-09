package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.ThrowingFunc;

import java.io.IOException;
import java.io.Serializable;

public class Command<T> implements Serializable
{
    private ThrowingFunc<T> command;
    public Command(ThrowingFunc<T> command){
        this.command = command;
    }
    public void invoke(T t) throws IOException, ClassNotFoundException {
        command.invoke(t);
    }
}