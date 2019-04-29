package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PowerUpCard {

    //public final PlayersFilter playersFilter;
    //public final SquaresFilter squaresFilter;
    //public final ShootFunc shootFunc;
    public final String name;
    public final AmmoColor color;
    public final Ammo cost;
    private List<Branch> branches;

    /*
    public PowerUpCard(String name, AmmoColor color, Ammo cost, PlayersFilter playersFilter, SquaresFilter squaresFilter, ShootFunc shootFunc)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.playersFilter = playersFilter;
        this.squaresFilter = squaresFilter;
        this.shootFunc = shootFunc;
    }
    */

    public PowerUpCard(String name, AmmoColor color, Ammo cost)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
        branches = new ArrayList<>();
    }

    public PowerUpCard(String name, AmmoColor color, Ammo cost, Branch branch)
    {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.branches = Collections.singletonList(branch);
    }

    public List<Branch> getBranches() {
        return branches;
    }
}
