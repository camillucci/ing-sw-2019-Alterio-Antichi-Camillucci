package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class SpawnAndShopSquare extends Square {
    private AmmoColor color;
    private List<WeaponCard> weapons;

    public SpawnAndShopSquare(int y, int x, AmmoColor color, SquareBorder[] borders, List<WeaponCard> weapons) {
        this.y = y;
        this.x = x;
        this.color = color;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
        this.weapons = weapons;
        this.players = new ArrayList<>();
    }

    @Override
    public void grab(Player player) {
        //TODO
    }
}
