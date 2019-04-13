package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.branch.Branch;

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
    public List<Branch> grab(Player player) {
        ArrayList<Branch> ret = new ArrayList<>();
        for(WeaponCard w: weapons)
            if(Ammo.getAmmo(player).isGreaterOrEqual(w.buyCost))
            {
                Action action = new Action(a -> a.getOwnerPlayer().addWeapon(a.getSelectedWeapons().get(0)));
                action.addWeapon(w);
                ret.add(new Branch(action, new EndBranchAction()));
            }
        return ret;
    }
}
