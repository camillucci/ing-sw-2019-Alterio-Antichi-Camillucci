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
    private final List<String> players = new ArrayList<>();
    private final List<String> colors = new ArrayList<>();
    private final List<String> cards;
    private final List<String> cardsCost;

    public SquareSnapshot(Square square)
    {
        this.ammoSquare = square.name.length() == 1;
        this.name = square.name;

        this.north = square.north.getName();
        this.south = square.south.getName();
        this.west = square.west.getName();
        this.east = square.east.getName();

        for(Player player : square.getPlayers()) {
            players.add(player.name);
            colors.add(player.color.getName());
        }

        cards = square.getCardsName();
        cardsCost = square.getCardsCost();
    }

    public List<String> getNames() {
        return players;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getCards() {
        return cards;
    }

    public List<String> getCardsCost() {
        return cardsCost;
    }
}
