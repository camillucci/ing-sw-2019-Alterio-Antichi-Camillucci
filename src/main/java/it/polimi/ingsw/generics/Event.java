package it.polimi.ingsw.generics;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Event<T, U>
{
    public void addEventHandler(BiConsumer<T, U> eventHandler)
    {
        this.eventHandlers.add(eventHandler);
    }

    public void invoke(T Sender, U args)
    {
        for(BiConsumer s: eventHandlers)
            s.accept(Sender, args);
    }

    private ArrayList<BiConsumer<T,U>> eventHandlers = new ArrayList<>();
}
