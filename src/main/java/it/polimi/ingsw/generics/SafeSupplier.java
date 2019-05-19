package it.polimi.ingsw.generics;

public interface SafeSupplier <E1 extends Exception, E2 extends Exception, R> {
    R get() throws E1, E2;
}
