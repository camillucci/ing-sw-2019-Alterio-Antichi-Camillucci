package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlayerColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all info relative to the game board state that clients are going to need. It implements
 * Serializable in order to be able to be sent to the client.
 */
public class GameBoardSnapshot implements Serializable
{
    /**
     * Snapshot of all the squares present on the map. The squares that aren't present are valued null.
     */
    public final SquareSnapshot[][] squareSnapshots = new SquareSnapshot[3][4];

    /**
     * Number of skulls that decides the game length
     */
    public final int skulls;

    /**
     * Integer that represents the map selected by the host. (They range from 1 to 3)
     */
    public final int mapType;
    private final List<List<String>> killShotTrack = new ArrayList<>();

    /**
     * Constructor. It selects all the relevant info for the clients relative to the current game board state.
     * @param gameBoard The game board from which all the infos are selected.
     */
    public GameBoardSnapshot(GameBoard gameBoard)
    {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                if(gameBoard.squares[i][j] != null)
                    squareSnapshots[i][j] = new SquareSnapshot(gameBoard.squares[i][j]);

        skulls = gameBoard.getSkulls();
        mapType = gameBoard.getMapType();

        List<List<PlayerColor>> temp = gameBoard.getKillShotTrack();
        for(int i = 0; i < temp.size(); i++) {
            killShotTrack.add(new ArrayList<>());
            for (PlayerColor color : temp.get(i))
                killShotTrack.get(i).add(color.getName());
        }
    }

    public List<List<String>> getKillShotTrack() {
        return killShotTrack;
    }
}
