package it.polimi.ingsw.model;

import it.polimi.ingsw.generics.Pair;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;
import it.polimi.ingsw.model.weapons.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponFactory
{
    private WeaponFactory(){}

    private static ArrayList<WeaponCard> weapons = new ArrayList<>();

    public static List<WeaponCard> getWeapons()
    {
        if(weapons.isEmpty())
            buildWeapons();
        return new ArrayList<>(weapons);
    }

    private static void buildWeapons()
    {
        weapons.add(new WeaponCard("Lock Rifle", new Ammo(1,0,0), new Ammo(2,0,0), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers, damage(2).andThen(mark(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(new ShootAction(visiblePlayers, damage(2).andThen(mark(1,1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Machine Gun", new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new ShootAction(visiblePlayers, damage(1)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers, damage(1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1),
                        new Branch(new ShootAction(visiblePlayers, damage(2)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers, damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new ShootAction(visiblePlayers, damage(2)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers, damage(2,1)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers, damage(2,1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1),
                        new Branch(new ShootAction(visiblePlayers, damage(2,2)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers, damage(2,2,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("T.H.O.R", new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(2, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2,1,2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Plasma Gun", new Ammo(0,0,1), new Ammo(1,0,1), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers, damage(2)),new EndBranchAction())), //S
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers, damage(2)), new EndBranchAction()), //M2S
                        new Branch(new ShootAction(visiblePlayers, damage(2)), new MoveAction(2), new EndBranchAction())), //SM2
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(new ShootAction(visiblePlayers, damage(3)),new EndBranchAction())), //S
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers, damage(3)), new EndBranchAction()), //M2S
                        new Branch(new ShootAction(visiblePlayers, damage(3)), new MoveAction(2), new EndBranchAction()))))); //SM2

        weapons.add(new WeaponCard("Whisper", new Ammo(1,0,1), new Ammo(2,0,1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(awayPlayers(2), damage(3).andThen(mark(1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Electroscythe", new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageAll(1), nearSquares(0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 1, 0), new Branch(new ShootAction(damageAll(2), nearSquares(0)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Tractor Beam", new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(tractorBeamPlayers1, tractorBeamSquares1, move.andThen(damage(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 1), new Branch(new ShootAction(tractorBeamPlayers2, tractorBeamSquares2, move.andThen(damage(3))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Vortex Cannon", new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(vortexCannonPlayers1, vortexCannonSquares1, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0),
                        new Branch(new ShootAction(vortexCannonPlayers2, vortexCannonSquares2, move.andThen(damage(2, 1))), new EndBranchAction()),
                        new Branch(new ShootAction(vortexCannonPlayers2, vortexCannonSquares2, move.andThen(damage(2, 1, 1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Furnace", new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageRoom(1), roomSquares), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageAll(1).andThen(markAll(1)), betweenSquares(1,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Heatseeker", new Ammo(0, 1, 1), new Ammo(0, 2, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(nonVisiblePlayers, damage(3)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Hellion", new Ammo(0, 0, 1), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(awayPlayers(1), damageMultiple(1, 0, 0, 1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0 ,1 ,0), new Branch(new ShootAction(awayPlayers(1), damageMultiple(1, 0, 0, 2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Flamethrower", new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new ShootAction(flamethrowerVisiblePlayers, damage(1)), new EndBranchAction()),
                        new Branch(new ShootAction(flamethrowerVisiblePlayers, damage(1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 2),
                        new Branch(new ShootAction(damageAll(2), flamethrowerVisibleSquares), new EndBranchAction()),
                        new Branch(new ShootAction(damageAll(2, 1), flamethrowerVisibleSquares), new EndBranchAction())))));

        weapons.add(new WeaponCard("Grenade Launcher", new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(grenadeLauncherPlayers, grenadeLauncherSquares, move.andThen(damage(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0),
                        new Branch(new ShootAction(grenadeLauncherPlayers, grenadeLauncherSquares, move.andThen(damage(1))), new ShootAction(damageAll(1), visibleSquares), new EndBranchAction()),
                        new Branch(new ShootAction(damageAll(1), visibleSquares), new ShootAction(grenadeLauncherPlayers, grenadeLauncherSquares, move.andThen(damage(1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Rocket Launcher", new Ammo(0, 1, 0), new Ammo(0, 2, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damage(2))), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1), new Branch(new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damageMultiple(2, 1, 0, 0))), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1),
                        new Branch(new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damageMultiple(2, 1, 0, 0))), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), new ShootAction(rocketLauncherPlayers, rocketLauncherSquares, move.andThen(damageMultiple(2, 1, 0, 0))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Railgun", new Ammo(1, 0, 1), new Ammo(1, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(railGunVisiblePlayers, damage(3)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(railGunVisiblePlayers, damage(2, 2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Cyberblade", new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new ShootAction(damage(2), nearSquares(0)), new EndBranchAction()),
                        new Branch(new ShootAction(damage(2), nearSquares(0)), new MoveAction(1), new EndBranchAction()),
                        new Branch(new MoveAction(1), new ShootAction(damage(2),nearSquares(0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1),
                        new Branch(new ShootAction(nearPlayers(0), damage(2, 2)), new EndBranchAction()),
                        new Branch(new ShootAction(nearPlayers(0), damage(2, 2)), new MoveAction(1), new EndBranchAction()),
                        new Branch(new MoveAction(1), new ShootAction(nearPlayers(0), damage(2, 2)), new EndBranchAction()),
                        new Branch(new ShootAction(nearPlayers(0), damage(2)), new MoveAction(1, 1), new ShootAction(nearPlayers(0),damage(2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("ZX-2", new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers, damage(1).andThen(mark(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers, mark(1, 1, 1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Shotgun", new Ammo(0, 0, 1), new Ammo(0, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(shotgunPlayers, shotgunSquares, move.andThen(damage(3))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(betweenPlayers(1, 1), damage(2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Power Glove", new Ammo(1, 0, 0), new Ammo(1, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(0), damage(1).andThen(mark(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(0), damage(2)), new EndBranchAction()),
                        new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(0), damage(2)), new MoveAction(1, 1), new EndBranchAction()),
                        new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(0), damage(2)), new MoveAction(1, 1), new ShootAction(nearPlayers(0), damage(2)), new EndBranchAction())))));
                        //TODO Add a way to move in the same direction

        weapons.add(new WeaponCard("Shockwave", new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(betweenPlayers(1,1), damage(1, 1, 1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1), new Branch(new ShootAction(damageAll(1, 1, 1, 1), betweenSquares(1,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Sledgehammer", new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(nearPlayers(0), damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(new ShootAction(sledgehammerPlayers, sledgehammerSquares, move.andThen(damage(3))), new EndBranchAction())))));
    }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF EFFECTS

    private static ShootFunc damage(Integer ... damage) { return (player, players, squares) -> Effects.damage(player, players, Arrays.asList(damage)); }
    private static ShootFunc mark(Integer ... marks) { return (player, players, squares) -> Effects.mark(player, players, Arrays.asList(marks)); }
    private static ShootFunc damageAll(Integer ... damage) { return (player, players, squares) -> Effects.damageAll(player, squares, Arrays.asList(damage)); }
    private static ShootFunc markAll(Integer ... marks) { return (player, players, squares) -> Effects.markAll(player, squares, Arrays.asList(marks)); }
    private static ShootFunc damageRoom (Integer ... damage) { return (player, players, squares) -> Effects.damageRoom(player, squares, Arrays.asList(damage)); }
    private static ShootFunc damageMultiple(Integer ... damage) { return (player, players, squares) -> Effects.damageMultiple(player, players, Arrays.asList(damage)); }
    private static ShootFunc move = (player, players, squares) -> Effects.move(players, squares);
    //private static ShootFunc moveAndDamage (Integer ... damage) { return (player, players, squares) -> Effects.moveAndDamage(player, players, squares, Arrays.asList(damage)); }
    //private static ShootFunc moveAndMultipleDamage (Integer ... damage) { return (player, players, squares) -> Effects.moveAndMultipleDamage(player, players, squares, Arrays.asList(damage)); }
    //private static ShootFunc moveToShooter = (player, players, squares) -> players.forEach(p->p.setCurrentSquare(player.getCurrentSquare()));
    //private static ShootFunc moveToSquare = (player, players, squares) -> players.forEach(p->p.setCurrentSquare(squares.get(0)));

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF PARAMETRIZED TARGET FILTERS

    private static PlayersFilter visiblePlayers = (player, players) -> TargetsFilters.visiblePlayers(player);
    private static PlayersFilter awayPlayers(int minDistance){ return (player, players) -> TargetsFilters.awayPlayers(player,minDistance);}
    private static PlayersFilter nearPlayers(int maxDistance) { return (player, players) -> TargetsFilters.nearPlayers(player, maxDistance); }
    private static PlayersFilter betweenPlayers(int minDistance, int maxDistance) { return (player, players) -> TargetsFilters.betweenPlayers(player, minDistance, maxDistance); }
    private static PlayersFilter nonVisiblePlayers = (player, players) -> TargetsFilters.nonVisiblePlayers(player);

    //private static PlayersFilter noFilters(int maxTargets) { return (player, players) -> TargetsFilters.noFilters(player, players, maxTargets); }

    private static SquaresFilter visibleSquares = (player, squares) -> TargetsFilters.visibleSquares(player);
    // private static SquaresFilter awaySquares(int minDistance) { return (player, squares) -> TargetsFilters.awaySquares(player, minDistance); }
    private static SquaresFilter nearSquares(int maxDistance) { return (player, squares) ->  TargetsFilters.nearSquares(player, maxDistance); }
    private static SquaresFilter betweenSquares(int minDistance, int maxDistance) { return (player, squares) -> TargetsFilters.betweenSquares(player, minDistance, maxDistance); }
    private static SquaresFilter roomSquares = (player, squares) -> TargetsFilters.otherVisibleRoom(player);

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF SPECIFIC TARGET FILTERS

    private static PlayersFilter thorVisiblePlayers = TargetsFilters::thorVisiblePlayers;

    private static PlayersFilter tractorBeamPlayers1 = (player, players) -> getLeftList(TargetsFilters.tractorBeamMovableTargets1(player));
    private static SquaresFilter tractorBeamSquares1 = (player, squares) -> getRightList(TargetsFilters.tractorBeamMovableTargets1(player));
    private static PlayersFilter tractorBeamPlayers2 = (player, players) -> getLeftList(TargetsFilters.tractorBeamMovableTargets2(player));
    private static SquaresFilter tractorBeamSquares2 = (player, squares) -> getRightList(TargetsFilters.tractorBeamMovableTargets2(player));

    private static PlayersFilter vortexCannonPlayers1 = (player, players) -> getLeftList(TargetsFilters.vortexCannonMovableTargets1(player));
    private static SquaresFilter vortexCannonSquares1 = (player, squares) -> getRightList(TargetsFilters.vortexCannonMovableTargets1(player));
    private static PlayersFilter vortexCannonPlayers2 = TargetsFilters::vortexCannonMovablePlayers2;
    private static SquaresFilter vortexCannonSquares2 = TargetsFilters::vortexCannonMovableSquares2;

    private static PlayersFilter flamethrowerVisiblePlayers = TargetsFilters::flamethrowerVisiblePlayers;
    private static SquaresFilter flamethrowerVisibleSquares = TargetsFilters::flamethrowerVisibleSquares;

    private static PlayersFilter grenadeLauncherPlayers = (player, players) -> getLeftList(TargetsFilters.grenadeLauncherMovableTargets(player));
    private static SquaresFilter grenadeLauncherSquares = (player, players) -> getRightList(TargetsFilters.grenadeLauncherMovableTargets(player));

    private static PlayersFilter rocketLauncherPlayers = (player, players) -> getLeftList(TargetsFilters.rocketLauncherMovableTargets(player));
    private static SquaresFilter rocketLauncherSquares = (player, players) -> getRightList(TargetsFilters.rocketLauncherMovableTargets(player));

    private static PlayersFilter railGunVisiblePlayers = (player, players) -> TargetsFilters.railgunVisiblePlayer(player);

    private static PlayersFilter shotgunPlayers = (player, players) -> getLeftList(TargetsFilters.shotgunMovableTargets(player));
    private static SquaresFilter shotgunSquares = (player, players) -> getRightList(TargetsFilters.shotgunMovableTargets(player));

    private static PlayersFilter sledgehammerPlayers = (player, players) -> getLeftList(TargetsFilters.sledgehammerMovableTargets(player));
    private static SquaresFilter sledgehammerSquares = (player, players) -> getRightList(TargetsFilters.sledgehammerMovableTargets(player));

    //------------------------------------------------------------------------------------------------------------------

    private static List<Player> getLeftList(List<Pair<Player, Square>> pairList) {
        List<Player> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getLeft());
        return temp;
    }

    private static List<Square> getRightList(List<Pair<Player, Square>> pairList) {
        List<Square> temp = new ArrayList<>();
        for(Pair<Player, Square> p : pairList)
            temp.add(p.getRight());
        return temp;
    }

    /*
    //private SquaresFilter tractorBeamVisiblePlayers1 = (player, players, squares) ->  TargetsFilters.tractorBeamVisiblePlayers1(player);

    //Shoot at visible targets
    private static ShootAction shootVisiblePlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.visiblePlayers(player), shootFunc);
    }

    //Shoot at visible squares
    private static ShootAction shootVisibleSquares(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, (player, squares) -> TargetsFilters.visibleSquares(player));
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

    //Shoot at visible squares which distant is >= minDistance //Not used
    /* private static ShootAction shootAwaySquares(BiConsumer<Player, List<Square>> shootFunc, int minDistance)
    {
        return new ShootAction(shootFunc, (player, squares) -> TargetsFilters.awaySquares(player, minDistance));
    } */
    /*

    //Shoot at visible squares which distant is <= maxDistance
    private static ShootAction shootNearSquares(BiConsumer<Player, List<Square>> shootFunc, int maxDistance)
    {
        return new ShootAction(shootFunc, (player, squares) -> TargetsFilters.nearSquares(player, maxDistance));
    }

    //Shoot at visible squares which distant is >= minDistance and <= maxDistance
    private static ShootAction shootBetweenSquares(BiConsumer<Player, List<Square>> shootFunc, int minDistance, int maxDistance)
    {
        return new ShootAction(shootFunc, (player, squares) -> TargetsFilters.betweenSquares(player, minDistance, maxDistance));
    }

    //Shoot at visible rooms
    private static ShootAction shootRoom(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, (player, squares) -> TargetsFilters.otherVisibleRoom(player));
    }

    //Shoot at non visible targets
    private static ShootAction shootNonVisiblePlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.nonVisiblePlayers(player), shootFunc);
    }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF SPECIFIC TARGET FILTERS

    private static ShootAction shootThor(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction(TargetsFilters::thorVisiblePlayers, shootFunc);
    }

    private static ShootAction shootTractorBeam1(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.tractorBeamVisiblePlayers1(player), shootFunc);
    }

    private static ShootAction shootTractorBeam2(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.tractorBeamVisiblePlayers2(player), shootFunc);
    }

    private static ShootAction shootVortexCannon1(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.vortexCannonVisiblePlayers1(player), shootFunc);
    }

    private static ShootAction shootVortexCannon2(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction(TargetsFilters::vortexCannonVisiblePlayers2, shootFunc);
    }

    private static ShootAction shootFlamethrowerPlayers(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction(TargetsFilters::flamethrowerVisiblePlayers, shootFunc);
    }

    private static ShootAction shootFlamethrowerSquares(BiConsumer<Player, List<Square>> shootFunc)
    {
        return new ShootAction(shootFunc, TargetsFilters::flamethrowerVisibleSquares);
    }

    private static ShootAction shootGrenadeLauncher(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.grenadeLauncherMovablePlayers(player), shootFunc);
    }

    private static ShootAction shootRocketLauncher(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.rocketLauncherMovablePlayers(player), shootFunc);
    }

    private static ShootAction shootRailgun(BiConsumer<Player, List<Player>> shootFunc)
    {
        return new ShootAction((player, players) -> TargetsFilters.railgunVisiblePlayer(player), shootFunc);
    }

    private static ShootAction shootShotgun(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.shotgunMovablePlayers(player), shootFunc);
    }

    private static ShootAction shootSledgehammer(TriConsumer<Player, List<Player>, List<Square>> shootFunc)
    {
        return new ShootAction((player, pair) -> TargetsFilters.sledgehammerMovablePlayers(player), shootFunc);
    }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF PARAMETRIZED EFFECTS

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

    private static TriConsumer<Player, List<Player>, List<Square>> moveAndMultipleDamage(Integer ... damage)
    {
        return (player, players, squares) -> Effects.moveAndMultipleDamage(player, players, squares, Arrays.asList(damage));
    }

    private static BiConsumer<Player, List<Player>> damageMultiple(Integer ... damage)
    {
        return (player, players) -> Effects.damageMultiple(player, players, Arrays.asList(damage));
    }
    */
}
