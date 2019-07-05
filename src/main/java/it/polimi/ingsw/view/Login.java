package it.polimi.ingsw.view;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;
import it.polimi.ingsw.snapshots.MatchSnapshot;

import java.io.IOException;
import java.util.List;

/**
 * Abstract class used to manage all the initial interactions with the user
 */
public abstract class Login implements ViewElement {
    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when the user chooses
     * between Socket or RMI connection
     */
    public final IEvent<Login, Boolean> socketEvent = new Event<>();

    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when the user chooses
     * their username
     */
    public final IEvent<Login, String> nameEvent = new Event<>();

    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when the user chooses
     * their player color
     */
    public final IEvent<Login, Integer> colorEvent = new Event<>();

    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when the host chooses
     * the number of skulls of which the game is going to be made of
     */
    public final IEvent<Login, Integer> gameLengthEvent = new Event<>();

    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when the host chooses
     * the map type the game is going to be played in
     */
    public final IEvent<Login, Integer> gameMapEvent = new Event<>();
    public final IEvent<Login, Boolean> rmiEvent = new Event<>();

    /**
     * Event other classes can subscribe to. This event is used to notify the subscribers when all the login tasks
     * have been finished.
     */
    public final IEvent<Login, MatchSnapshot> loginCompletedEvent = new Event<>();

    /**
     * Asks the user about which type of connection they want to use (RMI or Socket)
     */
    public abstract void askConnection() throws IOException;

    /**
     * This method is called in case the user's username has already been taken by someone else. It calls the
     * askName method one more time if the accepted parameter is false.
     * @param accepted Indicates whether the user's name of choice has been taken or not
     */
    public abstract void notifyAccepted(boolean accepted) throws IOException;

    /**
     * Method that describes what to do when the server notifies the client about the start of the login timer. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param time Amount of time left before the start of the match
     */
    public abstract void timerStartedMessage(int time);


    /**
     * Asks which color the user wants to take and sends the answer to the server via invoking the color Event.
     * @param availableColors List of colors the player can choose since they've not been taken by other players
     *                        in the room
     */
    public abstract void notifyAvailableColor(List<String> availableColors) throws IOException;

    /**
     * Method that describes what to do when the server notifies the client about the amount of time left in the timer.
     * The exact protocol to follow depends on the interface chosen by the user
     * @param time Amount of time left before the start of the match
     */
    public abstract void timerTickMessage(int time);

    /**
     * Method that describes what to do when the server notifies the client about a new player joining the room. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param name Name of the newly entered player
     */
    public abstract void newPlayerMessage(String name);

    /**
     * Provides a protocol to follow when a generic message is sent from the server to the client. The message is going
     * to be printed to the user regardless if the chosen interface is GUI or CLI.
     * @param message Message printed to the user
     */
    public abstract void onNewMessage(String message);

    /**
     * If the user is the host, then two questions are asked about how they want to set up the game. Then the server
     * is notified of the user's answer via invoking gameLenghtevent and gameMapEvent
     * @param isHost Indicates whether the user is the host (first one to enter the room) or not
     */
    public abstract void notifyHost(boolean isHost) throws IOException;

    /**
     * Calls the default intro methods. They display the intro script and ask the player for a username
     */
    public abstract void login() throws IOException;

    @Override
    public void onModelChanged(MatchSnapshot matchSnapshot) {
        ((Event<Login, MatchSnapshot>)loginCompletedEvent).invoke(this, matchSnapshot);
    }
}

