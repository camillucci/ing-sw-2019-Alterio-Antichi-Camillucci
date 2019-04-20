package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.List;

public class PowerUpCard {

    private final String name;
    private final AmmoColor color;
    private final Ammo cost;

    public PowerUpCard(String name, AmmoColor color, Ammo cost)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
    }

    public void shootP(Player shooter, List<Player> targets) {
        //TODO
    }

    public void shootS(Player shooter, List<Square> targets) {
        //TODO
    }

    public List<Player> visiblePlayers(Player player, List<Player> alreadyAdded) {
        //TODO
        return Collections.emptyList();
    }

    public List<Square> visibleSquares(Player player, List<Square> alreadyAdded) {
        //TODO
        return Collections.emptyList();
    }

    public AmmoColor getColor() {
        return color;
    }

    public Ammo getCost() {
        return cost;
    }
}
