package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WeaponCard {

    private final String name;
    public final Ammo buyCost;
    public final Ammo reloadCost;
    private List<FireModalityAction> fireModalities;

    public WeaponCard(String name, Ammo buyCost, Ammo reloadCost, List<FireModalityAction> fireModalities)
    {
        this.name = name;
        this.fireModalities = new ArrayList<>(fireModalities);
        this.buyCost = buyCost;
        this.reloadCost = reloadCost;
    }

    public WeaponCard(String name, Ammo buyCost, Ammo reloadCost, FireModalityAction ... fireModalityActions)
    {
        this(name, buyCost, reloadCost, Arrays.asList(fireModalityActions));
    }

    public List<Branch> getFireModalities()
    {
        return fireModalities.stream().map(Branch::new).collect(Collectors.toList());
    }
}
