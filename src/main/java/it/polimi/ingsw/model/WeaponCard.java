package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WeaponCard {

    public final String name;
    public final Ammo buyCost;
    public final Ammo reloadCost;
    private Supplier<List<FireModalityAction>> fireBuilder;

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

}
