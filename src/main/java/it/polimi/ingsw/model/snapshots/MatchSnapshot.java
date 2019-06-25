package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all info relative to the match state that clients are going to need. It implements
 * Serializable in order to be able to be sent to the client.
 */
public class MatchSnapshot implements Serializable {

    /**
     * Reference to the current game board state.
     */
    public final GameBoardSnapshot gameBoardSnapshot;

    /**
     * List of all players snapshots except the one relative to the current turn's player.
     */
    private final List<PublicPlayerSnapshot> publicPlayerSnapshot = new ArrayList<>();

    /**
     * Snapshot relative to the current turn's player.
     */
    public final PrivatePlayerSnapshot privatePlayerSnapshot;

    /**
     * Constructor. It selects all infos that the clients are going to need relative to the match they are playing in.
     * @param match Match from which the info is collected
     * @param currentPlayer Player corresponding to the player whose snapshot is going to be private.
     */
    public MatchSnapshot(Match match, Player currentPlayer) {
        gameBoardSnapshot = new GameBoardSnapshot(match.gameBoard);
        for(Player player : match.getPlayers())
            if(player != currentPlayer)
                publicPlayerSnapshot.add(new PublicPlayerSnapshot(player));
        privatePlayerSnapshot = new PrivatePlayerSnapshot(currentPlayer);
    }

    public List<PublicPlayerSnapshot> getPublicPlayerSnapshot()
    {
        return new ArrayList<>(publicPlayerSnapshot);
    }
}
