package it.polimi.ingsw.generics;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class Event<T, U>
{
    public void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        this.eventHandlers.add(eventHandler);
    }

    public void invoke(T sender, U args)
    {
        for(BiConsumer s: eventHandlers)
            s.accept(sender, args);
    }

    private ArrayList<BiConsumer<T,U>> eventHandlers = new ArrayList<>();
}
