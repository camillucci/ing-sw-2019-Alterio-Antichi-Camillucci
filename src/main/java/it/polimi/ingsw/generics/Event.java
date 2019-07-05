package it.polimi.ingsw.generics;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class Event<T, U> implements IEvent<T,U> {

    @Override
    public void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        eventHandlers.add(eventHandler);
    }

    @Override
    public void addTmpEventHandler(BiConsumer<T, U> eventHandler) {
        this.tmpEventHandlers.add(eventHandler);
    }

    @Override
    public void removeEventHandler(BiConsumer<T, U> eventHandler)
    {
        eventHandlers.remove(eventHandler);
    }

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
