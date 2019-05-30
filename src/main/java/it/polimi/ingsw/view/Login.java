package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.view.gui.login.NewLoginGUI;

import java.io.IOException;
import java.util.List;

public abstract class Login extends ViewElement
{
    public final IEvent<Login, Boolean> socketEvent = new Event<>();
    public final IEvent<Login, String> nameEvent = new Event<>();
    public final IEvent<Login, Integer> colorEvent = new Event<>();
    public final IEvent<Login, Integer> gameLengthEvent = new Event<>();
    public final IEvent<Login, Integer> gameMapEvent = new Event<>();
    public final IEvent<Login, Boolean> rmiEvent = new Event<>();
    public abstract void askConnection() throws IOException;
    public abstract void notifyAccepted(boolean accepted) throws IOException;
    public abstract void notifyAvailableColor(List<String> availableColors) throws IOException;
    public abstract void notifyHost(boolean isHost) throws IOException;
    public abstract void login() throws IOException;
}

