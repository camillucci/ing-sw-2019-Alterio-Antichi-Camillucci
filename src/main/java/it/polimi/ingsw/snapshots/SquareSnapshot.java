package it.polimi.ingsw.snapshots;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all info relative to a specific square that clients are going to need. It implements
 * Serializable in order to be able to be sent to the client.
 */
public class SquareSnapshot implements Serializable
{
    /**
     * Indicates whether ammo can be found on the square. In case it isn't an ammo square, it means the square is a
     * shop.
     */
    public final boolean ammoSquare;

    /**
     * Coordinates used to distinguish the square from all the others on the map.
     */
    public final String name;

    /**
     * String that represents what type of border is present right above the square
     */
    public final String north;

    /**
     * String that represents what type of border is present right below the square
     */
    public final String south;

    /**
     * String that represents what type of border the square's left border is
     */
    public final String west;

    /**
     * String that represents what type of border the square's right border is
     */
    public final String east;

    /**
     * List of players standing on the square
     */
    private final List<String> players = new ArrayList<>();

    /**
     * List of strings that represent the colors associated with the players standing on the square
     */
    private final List<String> colors = new ArrayList<>();

    /**
     * List of strings which represent the cards that can be grabbed from this square
     */
    private final List<String> cards;

    /**
     * List of strings which represent the costs of all the cards that can be grabbed from this square
     */
    private final List<String> cardsCost;

    /**
     * Constructor. It collects infos relative to the square that clients are going to need.
     * @param square Square from which the infos are going to be collected
     */
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
