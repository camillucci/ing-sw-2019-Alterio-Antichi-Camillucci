package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MatchSnapshot implements Serializable {

    public final GameBoardSnapshot gameBoardSnapshot;
    private final List<PublicPlayerSnapshot> publicPlayerSnapshot = new ArrayList<>();
    public final PrivatePlayerSnapshot privatePlayerSnapshot;

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
