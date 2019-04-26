package it.polimi.ingsw.model;

import java.io.Serializable;

public class SquareSnapshot implements Serializable
{
    public final SquareBorder[] borders;
    public final PlayerColor color; // null = no player

    public SquareSnapshot(SquareBorder[] borders, PlayerColor playerColor)
    {
        this.borders = borders;
        this.color = playerColor;
    }
}
