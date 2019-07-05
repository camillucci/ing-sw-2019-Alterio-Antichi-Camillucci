package it.polimi.ingsw.generics;

import java.util.function.BiConsumer;

/**
 * Utility class used to provide a notification based system. It's based on receiving a function when invoked and uses
 * the input parameters to execute said function.
 * @param <T> First parameter of the function
 * @param <U> Second parameter of the function
 */
public interface IEvent<T,U>
{
    /**
     * Adds the function gotten as input to the list of functions that need to be executed when the event is invoked
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    void addEventHandler(BiConsumer<T, U> eventHandler);

    /**
     * Adds the function gotten as input to a secondary list used for complex management of the handlers.
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    void addTmpEventHandler(BiConsumer<T, U> eventHandler);

    /**
     * Removes the function gotten as input from the list of functions that need to be executed when the event is
     * invoked
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    void removeEventHandler(BiConsumer<T, U> eventHandler);
}
