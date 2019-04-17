package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.weapons.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class WeaponFactory
{
    private WeaponFactory(){}
    private static ArrayList<WeaponCard> weapons = new ArrayList<>();

    public static List<WeaponCard> getWeapons()
    {
        buildWeapons();
        return new ArrayList<>(weapons);
    }

    private static void buildWeapons()
    {
        /*
        Definition of ShootAction example
        ShootAction ex = new ShootAction(TargetsFilters::thorVisiblePlayers, damage(2).andThen(mark(1,2,3)));
        damage(2) = damage P1 of two;
        mark(1,2,3) = mark P1 of 1, P2 of 2, P3 of 3
        damage(2).andThen(mark(1,2,3)) = damage P1 of 2 and then mark P1 of 1, P2 of 2, P3 of 3
        P1,..,Pn rest the same when andThen function is called
        */

        //TODO Add FireModalityAction costs to all weapons

        weapons.add(new WeaponCard("Lock Rifle", new Ammo(1,0,0), new Ammo(2,0,0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootVisiblePlayers(damage(2).andThen(mark(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(shootVisiblePlayers(damage(2).andThen(mark(1,1))), new EndBranchAction()))));

        weapons.add(new WeaponCard("Machine Gun", new Ammo(0,1,0), new Ammo(1,1,0),
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(shootVisiblePlayers(damage(1)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1),
                        new Branch(shootVisiblePlayers(damage(2)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(shootVisiblePlayers(damage(2)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(2,1)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(2,1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1),
                        new Branch(shootVisiblePlayers(damage(2,2)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(2,2,1)), new EndBranchAction()))));

        weapons.add(new WeaponCard("T.H.O.R", new Ammo(0,1,0), new Ammo(1,1,0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(2, 0, 0), new Branch(new ShootAction(TargetsFilters::thorVisiblePlayers, damage(2,1,2)), new EndBranchAction()))));

        weapons.add(new WeaponCard("Plasma Gun", new Ammo(0,0,1), new Ammo(1,0,1),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootVisiblePlayers(damage(2)),new EndBranchAction())), //S
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new MoveAction(2), shootVisiblePlayers(damage(2)), new EndBranchAction()), //M2S
                        new Branch(shootVisiblePlayers(damage(2)), new MoveAction(2), new EndBranchAction())), //SM2
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(shootVisiblePlayers(damage(3)),new EndBranchAction())), //S
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new MoveAction(2), shootVisiblePlayers(damage(3)), new EndBranchAction()), //M2S
                        new Branch(shootVisiblePlayers(damage(3)), new MoveAction(2), new EndBranchAction())))); //SM2

        weapons.add(new WeaponCard("Whisper", new Ammo(1,0,1), new Ammo(2,0,1),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootAwayPlayers(damage(3).andThen(mark(1)), 2), new EndBranchAction()))));

        weapons.add(new WeaponCard("Electroscythe", new Ammo(0, 0, 0), new Ammo(1, 0, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootNearSquares(damageAll(1), 0), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 1, 0), new Branch(shootNearSquares(damageAll(1), 0), new EndBranchAction()))));

        weapons.add(new WeaponCard("Tractor Beam", new Ammo(0, 0, 0), new Ammo(1, 0, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::tractorBeamVisiblePlayers, damage(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 1), new Branch(new ShootAction(TargetsFilters::tractorBeamVisiblePlayers, damage(3)), new EndBranchAction()))));

        //weapons.add(new WeaponCard("Furnace", new Ammo(0, 1, 0), new Ammo(1, 1, 0),
        //new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::otherVisibleRoom, damageRoom(1)), new EndBranchAction())),
        //new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::furnaceVisibleSquares, damageAll(1), markAll(1)), new EndBranchAction()))));

        //TODO Add all other weapons
    }

    //Shoot at visible targets
    private static ShootAction shootVisiblePlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.visiblePlayers(player), shootFunc);
    }

    //Shoot at visible squares
    private static ShootAction shootVisibleSquares(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, (player) -> TargetsFilters.visibleSquares(player));
    }

    //Shoot at visible players which distant is >= minDistance
    private static ShootAction shootAwayPlayers(BiConsumer<Player, List<Player>> shootFunc, int minDistance)
    {
        return new ShootAction((player, players) -> TargetsFilters.awayPlayers(player, minDistance), shootFunc);
    }

    //Shoot at visible players which distant is <= maxDistance
    private static ShootAction shootNearPlayers(BiConsumer<Player, List<Player>> shootFunc, int maxDistance)
    {
        return new ShootAction((player, players) -> TargetsFilters.nearPlayers(player, maxDistance), shootFunc);
    }

    //Shoot at visible squares which distant is >= minDistance
    private static ShootAction shootAwaySquares(BiConsumer<Player, List<Square>> shootFunc, int minDistance)
    {
        return new ShootAction(shootFunc, (player) -> TargetsFilters.awaySquares(player, minDistance));
    }

    //Shoot at visible squares which distant is <= maxDistance
    private static ShootAction shootNearSquares(BiConsumer<Player, List<Square>> shootFunc, int maxDistance)
    {
        return new ShootAction(shootFunc, (player) -> TargetsFilters.nearSquares(player, maxDistance));
    }

    private static BiConsumer<Player, List<Player>> damage(Integer ... damage)
    {
        return (player, players) -> Effects.damage(player, players, Arrays.asList(damage));
    }

    private static BiConsumer<Player, List<Player>> mark(Integer ... marks)
    {
        return (player, players) -> Effects.mark(player, players, Arrays.asList(marks));
    }

    private static BiConsumer<Player, List<Square>> damageAll(Integer ... damage)
    {
        return (player, squares) -> Effects.damageAll(player, squares, Arrays.asList(damage));
    }

    private static BiConsumer<Player, List<Square>> markAll(Integer ... marks)
    {
        return (player, squares) -> Effects.markAll(player, squares, Arrays.asList(marks));
    }
}
