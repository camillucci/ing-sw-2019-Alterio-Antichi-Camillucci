package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WeaponCard {

    public final String name;
    public final Ammo buyCost;
    public final Ammo reloadCost;
    public final Supplier<List<FireModalityAction>> fireBuilder;
    private static final String ANSI_RED = " \u001B[31m";
    private static final String ANSI_YELLOW = " \u001B[33m";
    private static final String ANSI_BLUE = " \u001B[34m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public WeaponCard(String name, Ammo buyCost, Ammo reloadCost, Supplier<List<FireModalityAction>> fireBuilder)
    {
        this.name = name;
        this.fireBuilder = fireBuilder;
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;
    }

    public List<Branch> getFireModalities()
    {
        return fireBuilder.get().stream().map(Branch::new).collect(Collectors.toList());
    }

    public List<Branch> getFireModalitysBranch(int i)
    {
        return fireBuilder.get().get(i).getBranches();
    }

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
