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

    @Override
    public void addTmpEventHandler(BiConsumer<T, U> eventHandler) {
        this.tmpEventHandlers.add(eventHandler);
    }

    @Override
    public synchronized void removeEventHandler(BiConsumer<T, U> eventHandler)
    {
        this.toRemoveEventHandlers.add(eventHandler);
    }

    public synchronized void invoke(T sender, U args)
    {
        eventHandlers.removeAll(toRemoveEventHandlers);
        toRemoveEventHandlers.clear();
        for(BiConsumer<T,U> s: eventHandlers)
            s.accept(sender, args);

        for(BiConsumer<T, U> s : tmpEventHandlers)
            s.accept(sender, args);

        tmpEventHandlers = new ArrayList<>();
    }

    private List<BiConsumer<T, U>> toRemoveEventHandlers = new ArrayList<>();
    private List<BiConsumer<T, U>> eventHandlers = new ArrayList<>();
    private List<BiConsumer<T, U>> tmpEventHandlers = new ArrayList<>();
}
