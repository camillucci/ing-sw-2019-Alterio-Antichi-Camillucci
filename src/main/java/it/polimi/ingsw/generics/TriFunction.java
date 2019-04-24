package it.polimi.ingsw.generics;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A,B,C,R> {

    R apply(A a, B b, C c);

}