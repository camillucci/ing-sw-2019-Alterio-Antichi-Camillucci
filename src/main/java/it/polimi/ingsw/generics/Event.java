package it.polimi.ingsw.generics;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

/**
 * Utility class used to provide a notification based system. It's based on receiving a function when invoked and uses
 * the input parameters to execute said function.
 * @param <T> First parameter of the function
 * @param <U> Second parameter of the function
 */
public class Event<T, U> implements IEvent<T,U> {

    /**
     * Adds the function gotten as input to the list of functions that need to be executed when the event is invoked
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    @Override
    public void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        eventHandlers.add(eventHandler);
    }

    /**
     * Adds the function gotten as input to a secondary list used for complex management of the handlers.
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    @Override
    public void addTmpEventHandler(BiConsumer<T, U> eventHandler) {
        this.tmpEventHandlers.add(eventHandler);
    }

    /**
     * Removes the function gotten as input from the list of functions that need to be executed when the event is
     * invoked
     * @param eventHandler Function that's going to be executed when the event is invoked
     */
    @Override
    public void removeEventHandler(BiConsumer<T, U> eventHandler)
    {
        eventHandlers.remove(eventHandler);
    }

    /**
     * For every function in the Handlers list, that function is executed with the input parameters. The same happens
     * for the functions in the tmpHandlers list.
     * @param sender First parameter used in the functions
     * @param args Second parameter used in the functions
     */
    public void invoke(T sender, U args)
    {
        for(BiConsumer<T, U> eventHandler : eventHandlers)
            eventHandler.accept(sender, args);
        for(BiConsumer<T, U> eventHandler : tmpEventHandlers)
            eventHandler.accept(sender, args);
    }

    public void clear(){
        eventHandlers.clear();
        tmpEventHandlers.clear();
    }

    private CopyOnWriteArrayList<BiConsumer<T, U>> eventHandlers = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<BiConsumer<T, U>> tmpEventHandlers = new CopyOnWriteArrayList<>();
}
