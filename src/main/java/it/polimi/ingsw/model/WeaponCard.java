package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponCard implements Visualizable {

    public final String name;
    public final Ammo buyCost;
    public final Ammo reloadCost;

    protected List<FireModalityAction> fireModalities;

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
        ArrayList<Branch> ret = new ArrayList<>();
        fireModalities.forEach(modality->ret.add(new Branch(modality)));
        return ret;
    }

    @Override
    public void visualize() {
        //TODO
    }
}
