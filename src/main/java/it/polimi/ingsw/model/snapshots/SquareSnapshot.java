package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SquareSnapshot implements Serializable
{
    public final boolean ammoSquare;
    public final String name;
    public final String north;
    public final String south;
    public final String west;
    public final String east;
    public final List<String> colors = new ArrayList<>(); // null = no player
    public final List<String> cards;

    public SquareSnapshot(Square square)
    {
        this.ammoSquare = square instanceof AmmoSquare;
        this.name = square.getName();

        this.north = square.north.getName();
        this.south = square.south.getName();
        this.west = square.west.getName();
        this.east = square.east.getName();

        for(Player player : square.getPlayers())
            colors.add(player.color.getName());

        cards = square.getCardsName();
    }
}
