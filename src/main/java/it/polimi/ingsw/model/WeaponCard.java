package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.FireModalityAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeaponCard implements Visualizable {

    protected String name;
    public final int blueBuyCost;
    public final int redBuyCost;
    public final int yellowBuyCost;
    public final int blueReloadCost;
    public final int redReloadCost;
    public final int yellowReloadCost;

    protected List<FireModalityAction> fireModalities = new ArrayList<>();

    public WeaponCard(String name, int[] ammoCosts, List<FireModalityAction> fireModalities)
    {
        this.name = name;
        this.fireModalities = new ArrayList<>(fireModalities);
        this.blueBuyCost = ammoCosts[0];
        this.redBuyCost = ammoCosts[1];
        this.yellowBuyCost = ammoCosts[2];
        this.blueReloadCost = ammoCosts[3];
        this.redReloadCost = ammoCosts[4];
        this.yellowReloadCost = ammoCosts[5];
    }
    public WeaponCard(String name, int[] ammoCosts, FireModalityAction first)
    {
        this(name, ammoCosts, Collections.singletonList(first));
    }

    public WeaponCard(String name, int[] ammoCosts, FireModalityAction first, FireModalityAction second)
    {
        this(name, ammoCosts, Arrays.asList(first,second));
    }

    public WeaponCard(String name, int[] ammoCosts, FireModalityAction first, FireModalityAction second, FireModalityAction third) {
        this(name, ammoCosts, Arrays.asList(first, second, third));
    }

    public WeaponCard(String name, int[] ammoCosts, FireModalityAction first, FireModalityAction second, FireModalityAction third, FireModalityAction fourth) {
        this(name, ammoCosts, Arrays.asList(first, second, third, fourth));
    }

    public List<Branch> getFireModalities()
    {
        ArrayList<Branch> ret = new ArrayList<>();
        for(FireModalityAction a : fireModalities)
            ret.add(new Branch(a));
        return ret;
    }

    @Override
    public void visualize() {
        //TODO
    }
}
