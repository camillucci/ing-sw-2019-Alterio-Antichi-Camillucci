package it.polimi.ingsw.generics;

public interface ThrowingAction<E1 extends Exception, E2 extends Exception, E3 extends Exception>
{
    void invoke() throws E1, E2, E3;
}
