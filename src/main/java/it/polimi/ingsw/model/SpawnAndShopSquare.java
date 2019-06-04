package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.ExtendableAction;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.cards.PowerUpCard;
import it.polimi.ingsw.model.cards.WeaponCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnAndShopSquare extends Square {

    private WeaponDeck weaponDeck;
    private List<WeaponCard> weapons = new ArrayList<>();
    private static final int CARDS_IN_SHOPS = 3;

    public SpawnAndShopSquare(String name, int y, int x, SquareBorder[] borders, WeaponDeck weaponDeck) {
        super(name, y, x, borders);
        this.weaponDeck = weaponDeck;
        for(int i = 0; i < CARDS_IN_SHOPS; i++)
            weapons.add(weaponDeck.draw());
        this.players = new ArrayList<>();
    }

    @Override
    public List<Branch> grab(Player player, List<PowerUpCard> powerUpCards) {
        return player.getWeapons().size() == 3 ? grabAndDrop(player, powerUpCards) : grabNoDrop(player, powerUpCards);
    }

    private List<Branch> grabAndDrop(Player player, List<PowerUpCard> powerUpCards)
    {
        List<Branch> ret = new ArrayList<>();
        ExtendableAction chooseToDropBranches =
                new ExtendableAction(player.getWeapons().stream().map(w->new Branch(new Action(a-> {
                    a.getOwnerPlayer().getCurrentSquare().addWeapon(w);
                    a.getOwnerPlayer().removeWeapon(w);
                }, "grab " + w.name), new EndBranchAction())).collect(Collectors.toList()), "discard a weapon");

        Ammo powerUpAmmo = new Ammo(0, 0, 0);
        for(PowerUpCard powerUpCard : powerUpCards)
            powerUpAmmo = powerUpAmmo.add(powerUpCard.colorToAmmo());
        for (WeaponCard w : weapons)
            if(powerUpAmmo.isLessOrEqualThan(w.buyCost) && player.getAmmo().isGreaterOrEqual(w.buyCost.sub(powerUpAmmo)))
                ret.add(new Branch(new Action(w.buyCost, a -> {
                    a.getOwnerPlayer().addWeapon(w);
                    a.getOwnerPlayer().getCurrentSquare().removeWeapon(w);
                }, "discard " + w.name), chooseToDropBranches));
        return ret;
    }

    private List<Branch> grabNoDrop(Player player, List<PowerUpCard> powerUpCards)
    {
        List<Branch> ret = new ArrayList<>();
        Ammo powerUpAmmo = new Ammo(0, 0, 0);
        for(PowerUpCard powerUpCard : powerUpCards)
            powerUpAmmo = powerUpAmmo.add(powerUpCard.colorToAmmo());
        for(WeaponCard w : weapons)
            if(powerUpAmmo.isLessOrEqualThan(w.buyCost) && player.getAmmo().isGreaterOrEqual(w.buyCost.sub(powerUpAmmo)))
                ret.add(new Branch(new Action(w.buyCost, a -> {
                    a.getOwnerPlayer().addWeapon(w);
                    a.getOwnerPlayer().getCurrentSquare().removeWeapon(w);
                }, "grab " + w.name), new EndBranchAction()));
        return ret;
    }

    @Override
    public void removeWeapon(WeaponCard weaponCard) {
        weapons.remove(weaponCard);
    }

    @Override
    public void addWeapon(WeaponCard weaponCard) {
        weapons.add(weaponCard);
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

    @Override
    public List<WeaponCard> getWeapons() { // Only for tests
        return new ArrayList<>(weapons);
    }
}
