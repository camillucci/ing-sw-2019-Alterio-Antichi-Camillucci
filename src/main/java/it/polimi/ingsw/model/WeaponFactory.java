package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.TriConsumer;
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

        weapons.clear();

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
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::tractorBeamVisiblePlayers1, damage(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 1), new Branch(new ShootAction(TargetsFilters::tractorBeamVisiblePlayers2, damage(3)), new EndBranchAction()))));
                //TODO Add a way to move others

        weapons.add(new WeaponCard("Vortex Cannon", new Ammo(1, 0, 0), new Ammo(1, 1, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootVisiblePlayers(damage(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0),
                        new Branch(shootVisiblePlayers(damage(2, 1)), new EndBranchAction()),
                        new Branch(shootVisiblePlayers(damage(2, 1, 1)), new EndBranchAction()))));
                //TODO Add a way to move others

        weapons.add(new WeaponCard("Furnace", new Ammo(1, 0, 0), new Ammo(1, 1, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootRoom(damageRoom(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootBetweenSquares(damageAll(1).andThen(markAll(1)), 1, 1), new EndBranchAction()))));

        weapons.add(new WeaponCard("Heatseeker", new Ammo(0, 1, 1), new Ammo(0, 2, 1),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootNonVisiblePlayers(damage(3)), new EndBranchAction()))));

        weapons.add(new WeaponCard("Hellion", new Ammo(0, 0, 1), new Ammo(0, 1, 1),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(shootAwayPlayers(damageMultiple(1, 0, 0, 1), 1), new EndBranchAction())),
                new FireModalityAction(new Ammo(0 ,1 ,0), new Branch(shootAwayPlayers(damageMultiple(1, 0, 0, 2), 1), new EndBranchAction()))));

        weapons.add(new WeaponCard("Flamethrower", new Ammo(0, 0, 0), new Ammo(0, 1, 0),
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(shootBetweenPlayers(damage(1), 1, 2), new EndBranchAction()),
                        new Branch(new ShootAction(TargetsFilters::flamethrowerVisiblePlayers, damage(1, 1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 2),
                        new Branch(shootBetweenSquares(damageAll(2), 1, 2), new EndBranchAction()),
                        new Branch(new ShootAction(damageAll(2, 1), TargetsFilters::flamethrowerVisibleSquares), new EndBranchAction()))));

        weapons.add(new WeaponCard("Grenade Launcher", new Ammo(0, 0, 0), new Ammo(0, 1, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::grenadeLauncherMovablePlayers, moveAndDamage(1, 0, 0, 0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0),
                        new Branch(new ShootAction(TargetsFilters::grenadeLauncherMovablePlayers, moveAndDamage(1, 0, 0, 0)), shootVisibleSquares(damageAll(1)), new EndBranchAction()),
                        new Branch(shootVisibleSquares(damageAll(1)), new ShootAction(TargetsFilters::grenadeLauncherMovablePlayers, moveAndDamage(1, 0, 0, 0)), new EndBranchAction()))));

        weapons.add(new WeaponCard("Rocket Launcher", new Ammo(0, 1, 0), new Ammo(0, 2, 0),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 0, 0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 0, 0)), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), shootAwayPlayers(damage(2), 1), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1),
                        new Branch(new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 1, 0)), new EndBranchAction()),
                        new Branch(new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 1, 0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1),
                        new Branch(new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 1, 0)), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), new ShootAction(TargetsFilters::rocketLauncherMovablePlayers, moveAndDamage(2, 0, 0, 0)), new EndBranchAction()))));

        //TODO Add all other weapons
    }

    //------------------------------------------------------------------------------------------------------------------

    //Shoot at visible targets
    private static ShootAction shootVisiblePlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.visiblePlayers(player), shootFunc);
    }

    //Shoot at visible squares
    private static ShootAction shootVisibleSquares(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, TargetsFilters::visibleSquares);
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

    //Shoot at visible players which distant is >= minDistance and <= maxDistance
    private static ShootAction shootBetweenPlayers(BiConsumer<Player, List<Player>> shootFunc, int minDistance, int maxDistance)
    {
        return new ShootAction((player, players) -> TargetsFilters.betweenPlayers(player, minDistance, maxDistance), shootFunc);
    }

    //Shoot at visible squares which distant is >= minDistance
    private static ShootAction shootAwaySquares(BiConsumer<Player, List<Square>> shootFunc, int minDistance)
    {
        return new ShootAction(shootFunc, player -> TargetsFilters.awaySquares(player, minDistance));
    }

    //Shoot at visible squares which distant is <= maxDistance
    private static ShootAction shootNearSquares(BiConsumer<Player, List<Square>> shootFunc, int maxDistance)
    {
        return new ShootAction(shootFunc, player -> TargetsFilters.nearSquares(player, maxDistance));
    }

    //Shoot at visible squares which distant is >= minDistance and <= maxDistance
    private static ShootAction shootBetweenSquares(BiConsumer<Player, List<Square>> shootFunc, int minDistance, int maxDistance)
    {
        return new ShootAction(shootFunc, player -> TargetsFilters.betweenSquares(player, minDistance, maxDistance));
    }

    //Shoot at visible rooms
    private static ShootAction shootRoom(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, TargetsFilters::otherVisibleRoom);
    }

    //Shoot at non visible targets
    private static ShootAction shootNonVisiblePlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.nonVisiblePlayers(player), shootFunc);
    }

    //------------------------------------------------------------------------------------------------------------------

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

    private static BiConsumer<Player, List<Square>> damageRoom(Integer ... damage)
    {
        return (player, squares) -> Effects.damageRoom(player, squares, Arrays.asList(damage));
    }

    private static TriConsumer<Player, List<Player>, List<Square>> moveAndDamage(Integer ... damage)
    {
        return (player, players, squares) -> Effects.moveAndDamage(player, players, squares, Arrays.asList(damage));
    }

    private static BiConsumer<Player, List<Player>> damageMultiple(Integer ... damage)
    {
        return (player, players) -> Effects.damageMultiple(player, players, Arrays.asList(damage));
    }
}
