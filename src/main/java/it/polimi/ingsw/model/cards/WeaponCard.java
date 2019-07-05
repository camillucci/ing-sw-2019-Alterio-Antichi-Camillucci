package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class contains all the info relative to a weapon card and all the methods that concern the fire modes the
 * weapon has.
 */
public class WeaponCard {

    /**
     * Name of the weapon, which distinguishes it from all the other weapon cards in the game
     */
    public final String name;

    /**
     * Amount of ammo necessary to buy the weapon from the shop.
     */
    public final Ammo buyCost;

    /**
     * Amount of ammo necessary to reaload the weapon when it is unloaded
     */
    public final Ammo reloadCost;
    public final Supplier<List<FireModalityAction>> fireBuilder;
    private static final String ANSI_RED = " \u001B[31m";
    private static final String ANSI_YELLOW = " \u001B[33m";
    private static final String ANSI_BLUE = " \u001B[34m";
    private static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Constructor. It assigns all the input parameters to their corresponding globals.
     * @param name String that's used to distinguish the weapon from all the others present in the game
     * @param buyCost Amount of ammo necessary to purchase the card
     * @param reloadCost Amount of ammo necessary to reload the card
     * @param fireBuilder Function used to create the fire modalities unique to the weapon
     */
    public WeaponCard(String name, Ammo buyCost, Ammo reloadCost, Supplier<List<FireModalityAction>> fireBuilder)
    {
        this.name = name;
        this.fireBuilder = fireBuilder;
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;
    }

    public List<Branch> getFireModalities(Player player, List<PowerUpCard> powerUpCards)
    {
        List<Branch> branches = new ArrayList<>();
        List<FireModalityAction> fireModalityActions = fireBuilder.get();
        Ammo playerPU = new Ammo(0, 0, 0);
        for(PowerUpCard powerUpCard : powerUpCards)
            playerPU = playerPU.add(powerUpCard.colorToAmmo());
        for(FireModalityAction fireModalityAction : fireModalityActions)
            if(player.getAmmo().add(playerPU).isGreaterOrEqual(fireModalityAction.getCost()) && playerPU.isLessOrEqualThan(fireModalityAction.getCost()))
                branches.add(new Branch(fireModalityAction));
        return branches;
    }

    public List<Branch> getFireModalitiesBranch(int i)
    {
        return fireBuilder.get().get(i).getBranches();
    }

    /**
     * Calculates the string equivalent of the ammo cost or reload cost of the weapon. The cost is calculated by
     * summing all the amounts ammo of each color.
     * @param buy Boolean that indicates whether the cost is referring to the cost associated with reloading or buying
     *            the weapon from the shop
     * @return The string equivalent of the requested cost.
     */
    public String buyCostToString(boolean buy) {
        Ammo cost = buy ? buyCost : reloadCost;
        String temp = "";
        for(int i = 0; i < cost.blue; i++)
            temp = temp.concat(ANSI_BLUE + "B");
        for(int i = 0; i < cost.red; i++)
            temp = temp.concat(ANSI_RED + "R");
        for(int i = 0; i < cost.yellow; i++)
            temp = temp.concat(ANSI_YELLOW + "Y");
        temp = temp.concat(ANSI_WHITE);
        return temp;
    }
}
