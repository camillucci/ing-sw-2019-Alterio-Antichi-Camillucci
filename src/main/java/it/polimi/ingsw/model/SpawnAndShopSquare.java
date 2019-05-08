package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ExtendableAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnAndShopSquare extends Square {

    private WeaponDeck weaponDeck;
    private List<WeaponCard> weapons = new ArrayList<>();
    private static final int CARDS_IN_SHOPS = 3;

    public SpawnAndShopSquare(int y, int x, SquareBorder[] borders, WeaponDeck weaponDeck) {
        super(y, x, borders);
        this.weaponDeck = weaponDeck;
        for(int i = 0; i < CARDS_IN_SHOPS; i++)
            weapons.add(weaponDeck.draw());
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
                ret.add(new Branch(new Action(a -> {
                    a.setDoActionCost(w.buyCost);
                    a.getOwnerPlayer().addWeapon(w);
                    a.getOwnerPlayer().getCurrentSquare().removeWeapon(w);
                }), chooseToDropBranches));
        return ret;
    }

    private List<Branch> grabNoDrop(Player player)
    {
        List<Branch> ret = new ArrayList<>();
        for(WeaponCard w : weapons)
            if(Ammo.getAmmo(player).isGreaterOrEqual(w.buyCost))
                ret.add(new Branch(new Action(a -> {
                    a.setDoActionCost(w.buyCost);
                    a.getOwnerPlayer().addWeapon(w);
                    a.getOwnerPlayer().getCurrentSquare().removeWeapon(w);
                }), new EndBranchAction()));
        return ret;
    }

    @Override
    public void removeWeapon(WeaponCard weaponCard) {
        weapons.remove(weaponCard);
    }

    @Override
    public void refill() {
        while(weapons.size() < 3)
            weapons.add(weaponDeck.draw());
    }

    @Override
    public List<String> getCardsName() {
        List<String> temp = new ArrayList<>();
        for(WeaponCard weaponCard : weapons)
            temp.add(weaponCard.name);
        return temp;
    }
}
