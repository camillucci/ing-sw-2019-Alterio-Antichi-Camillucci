package it.polimi.ingsw.model.snapshots;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SquareSnapshot implements Serializable
{
    public final boolean ammoSquare;
    public final SquareBorder north;
    public final SquareBorder south;
    public final SquareBorder west;
    public final SquareBorder east;
    public final List<PlayerColor> colors = new ArrayList<>(); // null = no player
    public final List<String> cards;

    public SquareSnapshot(Square square)
    {
        this.ammoSquare = square instanceof AmmoSquare;

        this.north = square.getNorth();
        this.south = square.getSouth();
        this.west = square.getWest();
        this.east = square.getEast();

        for(Player player : square.getPlayers())
            colors.add(player.color);

        cards = square.getCardsName();
    }
}
