package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class SpawnAndShopSquare extends Square {
    private AmmoColor color;
    private List<WeaponCard> weapons = new ArrayList<>();

    public SpawnAndShopSquare(AmmoColor color, SquareBorder north, SquareBorder sud, SquareBorder west, SquareBorder est, List<WeaponCard> weapons) {
        this.color = color;
        this.north = north;
        this.sud = sud;
        this.west = west;
        this.est = est;
        this.weapons = weapons;
    }

    @Override
    public void grab(Player player) {
        //TODO
    }
}
