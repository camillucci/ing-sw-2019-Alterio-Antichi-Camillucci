package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ExtendableAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnAndShopSquare extends Square {
    private List<WeaponCard> weapons;

    public SpawnAndShopSquare(int y, int x, SquareBorder[] borders, List<WeaponCard> weapons) {
        this.y = y;
        this.x = x;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
        this.weapons = weapons;
        this.players = new ArrayList<>();
    }

    @Override
    public List<Branch> grab(Player player) {
        return player.getWeapons().size() == 3 ? grabAndDrop(player) : grabNoDrop(player);
    }

    private List<Branch> grabAndDrop(Player player)
    {
        List<Branch> ret = new ArrayList<>();
        ExtendableAction chooseToDropBranches = new ExtendableAction(player.getWeapons().stream()
                                                                                        .map(w->new Branch(new Action(a->a.getOwnerPlayer().removeWeapon(w)), new EndBranchAction()))
                                                                                        .collect(Collectors.toList()));
        for (WeaponCard w : weapons)
            if(Ammo.getAmmo(player).isGreaterOrEqual(w.buyCost))
                ret.add(new Branch(new Action(a -> a.getOwnerPlayer().addWeapon(w)), chooseToDropBranches));
        return ret;
    }

    private List<Branch> grabNoDrop(Player player)
    {
        List<Branch> ret = new ArrayList<>();
        for(WeaponCard w : weapons)
            if(Ammo.getAmmo(player).isGreaterOrEqual(w.buyCost))
                ret.add(new Branch(new Action(a -> a.getOwnerPlayer().addWeapon(w)), new EndBranchAction()));
        return ret;
    }
}
