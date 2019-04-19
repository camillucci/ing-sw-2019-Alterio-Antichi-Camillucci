package it.polimi.ingsw.generics;

@FunctionalInterface
public interface TriConsumer<A,B,C> {
    void accept(A a, B b, C c);
}
