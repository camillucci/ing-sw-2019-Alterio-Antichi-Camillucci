package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.action.ShootAction;
import it.polimi.ingsw.model.weapons.PlayersFilter;
import it.polimi.ingsw.model.weapons.ShootFunc;
import it.polimi.ingsw.model.weapons.SquaresFilter;

import java.util.Collections;
import java.util.List;

public class PowerUpCard {

    public final PlayersFilter playersFilter;
    public final SquaresFilter squaresFilter;
    public final ShootFunc shootFunc;
    private final String name;
    private final AmmoColor color;
    private final Ammo cost;

    public PowerUpCard(String name, AmmoColor color, Ammo cost, PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shoootFunc)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
        this.shootFunc = shoootFunc;
    }

    public AmmoColor getColor() {
        return color;
    }

    public Ammo getCost() {
        return cost;
    }
}
