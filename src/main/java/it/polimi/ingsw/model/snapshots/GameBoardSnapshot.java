package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.PlayerColor;

import java.io.Serializable;
import java.util.List;

public class GameBoardSnapshot implements Serializable
{
    public final SquareSnapshot[][] squareSnapshots = new SquareSnapshot[3][4];
    public final int skulls;
    public final List<List<PlayerColor>> killShotTrack;

    public GameBoardSnapshot(GameBoard gameBoard)
    {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 4; j++)
                if(gameBoard.squares[i][j] != null)
                    squareSnapshots[i][j] = new SquareSnapshot(gameBoard.squares[i][j]);

        skulls = gameBoard.getSkulls();
        killShotTrack = gameBoard.getKillShotTrack();
    }
}
