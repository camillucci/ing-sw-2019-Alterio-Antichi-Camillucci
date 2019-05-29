package it.polimi.ingsw.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Event<T, U> implements IEvent<T,U> {

    private int eventHandlerIndex = 0;
    @Override
    public synchronized void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        eventHandlers.add(eventHandler);
    }

    @Override
    public void addTmpEventHandler(BiConsumer<T, U> eventHandler) {
        this.tmpEventHandlers.add(eventHandler);
    }

    @Override
    public synchronized void removeEventHandler(BiConsumer<T, U> eventHandler)
    {
        int index = eventHandlers.indexOf(eventHandler);
        if(index == -1)
            return;
        eventHandlers.remove(eventHandler);
        if(eventHandlerIndex >= index)
            eventHandlerIndex--;
    }

    public synchronized void invoke(T sender, U args)
    {
        for(eventHandlerIndex = 0; eventHandlerIndex < eventHandlers.size(); eventHandlerIndex++)
            eventHandlers.get(eventHandlerIndex).accept(sender, args);
        for(BiConsumer<T, U> eventHandler : tmpEventHandlers)
            eventHandler.accept(sender, args);
    }

    private List<BiConsumer<T, U>> eventHandlers = new ArrayList<>();
    private List<BiConsumer<T, U>> tmpEventHandlers = new ArrayList<>();
}
