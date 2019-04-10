package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.MarkAction;
import it.polimi.ingsw.model.action.DamageAction;
import it.polimi.ingsw.model.action.EndBranchAction;
import it.polimi.ingsw.model.action.SelectionAction;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory
{
    private static ArrayList<WeaponCard> weapons = new ArrayList<>();
    private static boolean weaponsCreated = false;

    public static List<WeaponCard> getWeapons()
    {
        buildWeapons();
        return new ArrayList<WeaponCard>(weapons);
    }

    private static void buildWeapons()
    {
        weapons.add(new WeaponCard("LockRifle",
                new SelectionAction(new Branch(new DamageAction(2), new MarkAction(1), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(2), new MarkAction(1,1), new EndBranchAction()), null)));
    }
}
