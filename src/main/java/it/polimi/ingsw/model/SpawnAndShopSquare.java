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

/**
 * This class represent one of the Squares where a player can spawn and buy WeaponCards,
 * it contains the WeaponCards the shop of the corresponding color contains
 */
public class SpawnAndShopSquare extends Square {

    /**
     * The deck with all remaining WeaponCards
     * It is used to refill the square at the end of the turn
     */
    private WeaponDeck weaponDeck;
    /**
     * The list of WeaponCards a Player can grab from this Square
     */
    private List<WeaponCard> weapons = new ArrayList<>();
    private static final int CARDS_IN_SHOPS = 3;

    /**
     * Create a new SpawnAndShopSquare as a part of the GameBoard, it also draw the first three WeaponCards
     * @param name The name of the square
     * @param y The first coordinate
     * @param x The second coordinate
     * @param borders The 4 SquareBorder, one for each cardinal direction
     * @param weaponDeck The WeaponDeck for refill the shop
     */
    public SpawnAndShopSquare(String name, int y, int x, SquareBorder[] borders, WeaponDeck weaponDeck) {
        super(name, y, x, borders);
        this.weaponDeck = weaponDeck;
        for(int i = 0; i < CARDS_IN_SHOPS; i++)
            weapons.add(weaponDeck.draw());
        this.players = new ArrayList<>();
    }

    /**
     * This method give to a given Player the choices for grabbing a WeaponCard contained in the shop
     * it is used through a GrabAction,
     * it divide itself in grabAndDrop or grabAndNoDrop based on the WeaponCards the given Player already has
     * @param player The Player in which add the WeaponCard
     * @param powerUpCards The list of discarded PowerUpCard for reducing the WeaponCards cost
     * @return A single Branch from which the Player can choose which WeaponCard to grab
     */
    @Override
    public List<Branch> grab(Player player, List<PowerUpCard> powerUpCards) {
        return player.getWeapons().size() == 3 ? grabAndDrop(player, powerUpCards) : grabNoDrop(player, powerUpCards);
    }

    /**
     * This method return both the choice for grabbing and for discarding a WeaponCard
     * @param player The Player in which add the WeaponCard
     * @param powerUpCards The list of discarded PowerUpCard for reducing the WeaponCards cost
     * @return A single Branch from which the Player can choose which WeaponCard to grab and drop
     */
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

    /**
     * This method return both the choice for grabbing a WeaponCard
     * @param player The Player in which add the WeaponCard
     * @param powerUpCards The list of discarded PowerUpCard for reducing the WeaponCards cost
     * @return A single Branch from which the Player can choose which WeaponCard to grab
     */
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

    /**
     * This method removes a given WeaponCard when it is grabbed
     * @param weaponCard The WeaponCard to remove
     */
    @Override
    public void removeWeapon(WeaponCard weaponCard) {
        weapons.remove(weaponCard);
    }

    /**
     * This method adds a given WeaponCard when it is dropped
     * @param weaponCard The WeaponCard to add
     */
    @Override
    public void addWeapon(WeaponCard weaponCard) {
        weapons.add(weaponCard);
    }

    /**
     * This method refill the SpawnAndShopSquare at the end of each Turn with new WeaponCards
     */
    @Override
    public void refill() {
        while(weapons.size() < 3)
            weapons.add(weaponDeck.draw());
    }

    /**
     * This method return a list of names of the WeaponCards this SpawnAndShopSquare contains
     * @return The list of names of the WeaponCards this SpawnAndShopSquare contains
     */
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
