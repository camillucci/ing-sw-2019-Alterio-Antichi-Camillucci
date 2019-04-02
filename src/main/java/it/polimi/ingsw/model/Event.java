package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Event
{
    public void addEventHandler(BiConsumer<Object, Object> eventHandler)
    {
        this.eventHandlers.add(eventHandler);
    }

    public void invoke(Object Sender, Object args)
    {
        for(BiConsumer s: eventHandlers)
            s.accept(Sender, args);
    }

    private ArrayList<BiConsumer<Object,Object>> eventHandlers = new ArrayList<>();
}
