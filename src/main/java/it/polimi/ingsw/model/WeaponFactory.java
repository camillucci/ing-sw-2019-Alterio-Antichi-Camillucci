package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.weapons.Effects;
import it.polimi.ingsw.model.weapons.TargetsFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class WeaponFactory
{
    private static ArrayList<WeaponCard> weapons = new ArrayList<>();
    public static List<WeaponCard> getWeapons()
    {
        buildWeapons();
        return new ArrayList<>(weapons);
    }

    private static void buildWeapons()
    {
        // Definition of ShootAction example //
        ShootAction ex = new ShootAction(TargetsFilters::thorVisiblePlayers, damageF(2).andThen(markF(1,2,3)));
        // damageF(2) = damage P1 of two;
        //markF(1,2,3) = mark P1 of 1, P2 of 2, P3 of 3
        // damageF(2).andThen(markF(1,2,3)) = damage P1 of 2 and then mark P1 of 1, P2 of 2, P3 of 3
        // P1,..,Pn rest the same when andThen function is called()


        weapons.add(new WeaponCard("LockRifle",  new int[] {1, 0, 0, 2, 0, 0},
                new FireModalityAction(new Branch(shootDef(damageF(2).andThen(markF(1))), new EndBranchAction()), null), //TODO cost
                new FireModalityAction(new Branch(shootDef(damageF(2).andThen(markF(1,1))), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("MachineGun", new int[] {0, 1, 0, 1, 1, 0},
                new FireModalityAction(new Branch(shootDef(damageF(1,1)), new EndBranchAction()), null),
                new FireModalityAction(new Branch(shootDef(damageF(2,1)), new EndBranchAction()), null),
                new FireModalityAction(new Branch(shootDef(damageF(1,2,1)), new EndBranchAction()), null),
                new FireModalityAction(new Branch(shootDef(damageF(2,2,1)), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("T.H.O.R", new int[] {0, 1, 0, 1, 1, 0},
                new FireModalityAction(new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damageF(2)), new EndBranchAction()), null),
                new FireModalityAction(new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damageF(2,1)), new EndBranchAction()), null),
                new FireModalityAction(new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damageF(2,1,2)), new EndBranchAction()), null)));

        weapons.add(new WeaponCard("PlasmaGun", new int[] {0, 0, 1, 1, 0, 1},
                new FireModalityAction(new Branch(shootDef(damageF(2)),new EndBranchAction()), null), //S
                new FireModalityAction(
                        new Branch(new MoveAction(2), shootDef(damageF(2)), new EndBranchAction()), //M2S
                        new Branch(shootDef(damageF(2)), new MoveAction(2), new EndBranchAction()), null), //SM2
                new FireModalityAction(
                        new Branch(new MoveAction(2), shootDef(damageF(3)), new EndBranchAction()), //M2S
                        new Branch(shootDef(damageF(3)), new MoveAction(2), new EndBranchAction()), null))); //SM2
/*
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
 */
    }

    private static ShootAction shootDef(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.visiblePlayers(player), shootFunc);
    }
    public static BiConsumer<Player, List<Player>> damageF(int ... damage)
    {
        ArrayList<Integer> tmp = new ArrayList<>();
        for(Integer i : damage)
            tmp.add(i);
        return (player, players) -> Effects.damage(player, players, tmp);
    }
    private static BiConsumer<Player, List<Player>> markF(int ... marks)
    {
        ArrayList<Integer> tmp = new ArrayList<>();
        for(Integer i : marks)
            tmp.add(i);
        return (player, players) -> Effects.mark(player, players, tmp);
    }

}
