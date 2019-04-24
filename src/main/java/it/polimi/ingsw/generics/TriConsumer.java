package it.polimi.ingsw.generics;

@FunctionalInterface
public interface TriConsumer<A,B,C> {
    void accept(A a, B b, C c);

    default TriConsumer<A,B,C> andThen(TriConsumer<? super A, ? super B, ? super C> after)
    {
        return (a,b,c) -> {accept(a,b,c); after.accept(a,b,c);};
    }
}
