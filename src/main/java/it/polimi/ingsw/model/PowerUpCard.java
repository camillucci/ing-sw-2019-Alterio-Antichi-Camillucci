package it.polimi.ingsw.model;

import java.util.List;

public abstract class PowerUpCard {

    public final String name;
    public final AmmoColor color;
    public final Ammo cost;
    public void shootP(Player shooter, List<Player> targets){}
    public void shootS(Player shooter, List<Square> targets){}
    public abstract List<Player> visiblePlayers(Player player, List<Player> alreadyAdded);
    public abstract List<Square> visibleSquares(Player player);

    public PowerUpCard(String name, Ammo cost, AmmoColor color)
    {
        this.name = name;
        this.cost = cost;
        this.color = color;
    }
}
