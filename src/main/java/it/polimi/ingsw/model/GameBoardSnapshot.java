package it.polimi.ingsw.model;

import java.io.Serializable;

public class GameBoardSnapshot implements Serializable
{
    public final SquareSnapshot[][] squares;
    public GameBoardSnapshot(SquareSnapshot[][] squares)
    {
        this.squares = squares;
    }
}
