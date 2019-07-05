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
     * Method that describes what to do when the server notifies the client about an update about the game state. The
     * exact protocol to follow depends on the interface chosen by the user
     * @param matchSnapshot The new matchSnapshot that the scribe the new model state
     */
    void onModelChanged(MatchSnapshot matchSnapshot);
}
