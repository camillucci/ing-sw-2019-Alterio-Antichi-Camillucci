package it.polimi.ingsw.model;

import java.util.List;

public class SpawnAndShopSquare extends Square {
    private AmmoColor color;
    private List<WeaponCard> weapons;

    public SpawnAndShopSquare(AmmoColor color, SquareBorder north, SquareBorder south, SquareBorder west, SquareBorder east, List<WeaponCard> weapons) {
        this.color = color;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        this.weapons = weapons;
    }

    @Override
    public void grab(Player player) {
        //TODO
    }
}
