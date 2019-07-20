package it.polimi.ingsw.network;

import it.polimi.ingsw.generics.ThrowingFunc;

import java.io.IOException;
import java.io.Serializable;

public class Command<T> implements Serializable
{
    private ThrowingFunc<T> throwingFunc;
    public Command(ThrowingFunc<T> throwingFunc){
        this.throwingFunc = throwingFunc;
    }
    public void invoke(T t) throws IOException, ClassNotFoundException {
        throwingFunc.invoke(t);
    }
}