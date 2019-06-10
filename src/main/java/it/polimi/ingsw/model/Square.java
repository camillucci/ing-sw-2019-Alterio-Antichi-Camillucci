package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.SquareBorder.*;

/**
 * This class describes a generic Square,
 * it must be specialized in either a SpawnAndShopSquare or a AmmoSquare
 */
public abstract class Square {
    /**
     * The name of the Square
     */
    public final String name;
    /**
     * The y coordinate of the Square
     */
    public final int y;
    /**
     * The x coordinate of the Square
     */
    public final int x;
    /**
     * The type of border it confine with the north Square
     */
    public final SquareBorder north;
    /**
     * The type of border it confine with the south Square
     */
    public final SquareBorder south;
    /**
     * The type of border it confine with the west Square
     */
    public final SquareBorder west;
    /**
     * The type of border it confine with the east Square
     */
    public final SquareBorder east;
    /**
     * The list of Player this Square currently contains
     */
    protected List<Player> players;

    /**
     * This constructor is used as a common constructor for both Square's subclasses
     * @param name The name of the square
     * @param y The first coordinate
     * @param x The second coordinate
     * @param borders The 4 SquareBorder, one for each cardinal direction
     */
    protected Square(String name, int y, int x, SquareBorder[] borders) {
        this.name = name;
        this.y = y;
        this.x = x;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
    }

    /**
     *
     * @param player The Player in which add the resources
     * @param powerUpCards The list of discarded PowerUpCard for reducing the WeaponCards cost
     * @return A single Branch from which the Player can choose what to grab
     */
    public abstract List<Branch> grab(Player player, List<PowerUpCard> powerUpCards);

    /**
     * This method checks if this Square is linked to the north Square
     * @return True if this Square is linked to the north Square
     */
    public boolean okNorth() {
        return this.north == DOOR || this.north == ROOM;
    }

    /**
     * This method checks if this Square is linked to the south Square
     * @return True if this Square is linked to the south Square
     */
    public boolean okSouth() {
        return this.south == DOOR || this.south == ROOM;
    }

    /**
     * This method checks if this Square is linked to the west Square
     * @return True if this Square is linked to the west Square
     */
    public boolean okWest() {
        return this.west == DOOR || this.west == ROOM;
    }

    /**
     * This method checks if this Square is linked to the east Square
     * @return True if this Square is linked to the east Square
     */
    public boolean okEast() {
        return this.east == DOOR || this.east == ROOM;
    }

    /**
     * This method checks if the northern Square exists
     * @return True if the northern Square exists
     */
    public boolean existNorth() {
        return this.north != NOTHING;
    }

    /**
     * This method checks if the southern Square exists
     * @return True if the southern Square exists
     */
    public boolean existSouth() {
        return this.south != NOTHING;
    }

    /**
     * This method checks if the western Square exists
     * @return True if the western Square exists
     */
    public boolean existWest() {
        return this.west != NOTHING;
    }

    /**
     * This method checks if the eastern Square exists
     * @return True if the eastern Square exists
     */
    public boolean existEast() {
        return this.east != NOTHING;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * This method adds a given Player to this Square when the Player move or is moved on it
     * @param player The Player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * This method removes a given Player from this Square when the Player move or is moved from it
     * @param player The Player to remove
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * This method returns the list of names of cards on this Square
     * @return The list of names of cards on this Square
     */
    public abstract List<String> getCardsName();

    /**
     * This method removes a given WeaponCard when it is grabbed, only for SpawnAndShopSquare
     * @param weaponCard The WeaponCard to remove
     */
    public void removeWeapon(WeaponCard weaponCard) { }

    /**
     * This method adds a given WeaponCard when it is dropped, only for SpawnAndShopSquare
     * @param weaponCard The WeaponCard to add
     */
    public void addWeapon(WeaponCard weaponCard) { }

    public List<WeaponCard> getWeapons() { return Collections.emptyList(); }

    /**
     * This method refills the Square at the end of each Turn
     */
    public abstract void refill();
}
