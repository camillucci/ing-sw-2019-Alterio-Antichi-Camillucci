package it.polimi.ingsw.network;

import java.io.IOException;
import java.util.List;

/**
 * This class is an interface for the server and contains all the methods that can be used regardless of which
 * connection type the user has chosen. Most of the methods, in fact, communicate parameters and values to the room
 * the server is associated with.
 */
public interface IAdrenalineServer
{
    /**
     *
     * @return The list of strings equivalent to the colors that are still available to pick in the room the server is
     * associated with.
     */
    List<String> availableColors();

    /**
     * Communicates user's decision regarding which color they want to the controller
     * @param colorIndex Index of the color that has been chosen by the user
     */
    void setColor(int colorIndex) throws IOException;

    /**
     * Communicates user's decision regarding their username to the controller
     * @param name Name chosen by the user
     */
    void setName(String name) throws IOException;

    /**
     * Communicates host's decision regarding how many skulls they want in the game to the controller
     * @param gameLength Number of skulls that have been selected by the host
     */
    void setGameLength(int gameLength);

    /**
     * Communicates host's decision regarding which map type they want to play in to the controller
     * @param choice Type of the map that has been selected by the host
     */
    void setGameMap(int choice);
    void ready();
    void newActionCommand(Command<RemoteActionsHandler> command);
}
