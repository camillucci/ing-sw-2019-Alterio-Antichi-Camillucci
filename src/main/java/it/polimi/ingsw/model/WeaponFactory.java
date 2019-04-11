package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.List;

public class WeaponFactory
{
    private static ArrayList<WeaponCard> weapons = new ArrayList<>();
    private static boolean weaponsCreated = false;

    public static List<WeaponCard> getWeapons()
    {
        if(!weaponsCreated)
            buildWeapons();
        return new ArrayList<>(weapons);
    }

    private static void buildWeapons()
    {
        weapons.add(new WeaponCard("LockRifle", new int[] {1, 0, 0, 2, 0, 0},
                new SelectionAction(new Branch(new DamageAction(2), new MarkAction(1), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(2), new MarkAction(1,1), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("MachineGun", new int[] {0, 1, 0, 1, 1, 0},
                new SelectionAction(new Branch(new DamageAction(1), new DamageAction(1), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(1), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(1), new DamageAction(1), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("T.H.O.R", new int[] {0, 1, 0, 1, 1, 0},
                new SelectionAction(new Branch(new DamageAction(2), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(1), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(2), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("PlasmaGun", new int[] {0, 0, 1, 1, 0, 1},
                new SelectionAction(new Branch(new DamageAction(2), new EndBranchAction()), null),
                new SelectionAction(new Branch(new MoveAction(2), new EndBranchAction()), null),
                new SelectionAction(new Branch(new DamageAction(1), new EndBranchAction()), null)));
        //TODO need to specify the moveAction is possible both before and after the other effects
    }
}
