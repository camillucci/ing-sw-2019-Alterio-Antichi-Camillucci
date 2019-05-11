package it.polimi.ingsw.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Event<T, U> implements IEvent<T,U> {

    @Override
    public synchronized void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        this.eventHandlers.add(eventHandler);
    }

    public synchronized void removeEventHandler(BiConsumer<T, U> eventHandler)
    {
        this.eventHandlers.remove(eventHandler);
    }

    public synchronized void invoke(T sender, U args)
    {
        for(BiConsumer s: eventHandlers)
            s.accept(sender, args);
    }

    private List<BiConsumer<T, U>> eventHandlers = new ArrayList<>();

}
