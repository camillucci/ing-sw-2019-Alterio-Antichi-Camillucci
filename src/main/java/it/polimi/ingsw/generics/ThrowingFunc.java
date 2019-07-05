package it.polimi.ingsw.generics;

import java.io.IOException;
import java.io.Serializable;

public interface ThrowingFunc<T> extends Serializable {
    void invoke(T t) throws IOException, ClassNotFoundException;
}
