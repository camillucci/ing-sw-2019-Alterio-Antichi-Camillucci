package it.polimi.ingsw.view;

import it.polimi.ingsw.snapshots.MatchSnapshot;

public interface ViewElement {
    /**
     * Provides a protocol to follow when a generic message is sent from the server to the client. The message is going
     * to be printed to the user regardless if the chosen interface is GUI or CLI.
     * @param message Message printed to the user
     */
    void onNewMessage(String message);

    /**
     * Method that describes what to do when the server notifies the client about the disconnection of a player. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param name Name of the disconnected player
     */
    void disconnectedPlayerMessage(String name);

    /**
     * Method that describes what to do when the server notifies the client about a new player joining the room. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param name Name of the newly entered player
     */
    void newPlayerMessage(String name);

    /**
     * Method that describes what to do when the server notifies the client about the start of the login timer. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param time Amount of time left before the start of the match
     */
    void timerStartedMessage(int time);

    /**
     * Method that describes what to do when the server notifies the client about the amount of time left in the timer.
     * The exact protocol to follow depends on the interface chosen by the user
     * @param time Amount of time left before the start of the match
     */
    void timerTickMessage(int time);

    /**
     * Method that describes what to do when the server notifies the client about the reconnection of a player. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param name Name of the reconnected player
     */
    void reconnectedMessage(String name);

    /**
     * Method that describes what to do when the server notifies the client about the declaration of a winner. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param winner Name of the winner of the match
     */
    void winnerMessage(String winner);

    /**
     * Method that describes what to do when the server sends the client the scoreboard relative to the match. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param scoreboard Matrix of Strings that represents the scoreboard relative to the match this client is playing
     *                   in
     */
    void scoreboardMessage(String[][] scoreboard);

    /**
     * Method that describes what to do when the server notifies the client about an update about the game state. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param matchSnapshot The new matchSnapshot that the scribe the new model state
     */
    void onModelChanged(MatchSnapshot matchSnapshot);
}
