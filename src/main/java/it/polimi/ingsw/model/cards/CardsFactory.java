package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Ammo;
import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.model.AmmoColor.*;
import static it.polimi.ingsw.model.AmmoColor.YELLOW;

/**
 * This cards generates the default set of cards that the game is played with. It features methods that generate the
 * 3 different type cards when called (ammo, power up, weapon).
 */
public class CardsFactory {

    private CardsFactory(){}

    /**
     * List of weapon cards that this class generates when the get method is called for the first time (the cards
     * generated are always the same)
     */
    private static List<WeaponCard> weapons = new ArrayList<>();

    /**
     * List of power up cards that this class generates when the get method is called for the first time (the cards
     * generated are always the same)
     */
    private static List<PowerUpCard> powerUpCards = new ArrayList<>();

    /**
     * List of ammo cards that this class generates when the get method is called for the first time (the cards
     * generated are always the same)
     */
    private static List<AmmoCard> ammo = new ArrayList<>();

    /**
     * Default integer that represents how many copy of each different ammo card the game contains
     */
    private static final int COPY_OF_AMMO = 3;

    /**
     * Calls the buildWeapons method to generate the weapons in case it is the first time the get method has been called
     * @return The list of weapon cards the game contains
     */
    public static List<WeaponCard> getWeapons()
    {
        if(weapons.isEmpty())
            buildWeapons();
        return new ArrayList<>(weapons);
    }

    /**
     * Calls the buildPowerUps method to generate the weapons in case it is the first time the get method has
     * been called
     * @return The list of power up cards the game contains
     */
    public static List<PowerUpCard> getPowerUps()
    {
        if(powerUpCards.isEmpty())
            buildPowerUps();
        return new ArrayList<>(powerUpCards);
    }

    /**
     * Calls the buildAmmo method to generate the weapons in case it is the first time the get method has been called
     * @return The list of ammo cards the game contains
     */
    public static List<AmmoCard> getAmmo()
    {
        if(ammo.isEmpty())
            buildAmmo();
        return new ArrayList<>(ammo);
    }

    /**
     * Generates the default list of power up cards based on the original rules of the game. 2 copies of each power up
     * card are generated for each ammo color.
     */
    private static void buildPowerUps()
    {
        List <AmmoColor> allColors = new ArrayList<>(Arrays.asList(BLUE, RED, YELLOW, BLUE, RED, YELLOW));
        for(AmmoColor ammoColor : allColors) {
            powerUpCards.add(new InTurnPowerUpCard("Targeting Scope", ammoColor, damageNoMark(1)));
            powerUpCards.add(new EndStartPowerUpCard("Newton", ammoColor, otherPlayers, near2Squares, move));
            powerUpCards.add(new CounterAttackPowerUpCard("Tagback Grenade", ammoColor, mark(1)));
            powerUpCards.add(new EndStartPowerUpCard("Teleporter", ammoColor, TargetsFilters.noPlayersFilter, allSquares, moveSelf));
        }
    }

    /**
     * Generates the default list of ammo cards based on the original rules of the game. 3 copies of each ammo card
     * are generated.
     */
    private static void buildAmmo()
    {
        for (int i = 0; i < COPY_OF_AMMO; i++) {
            ammo.add(new AmmoCard(new Ammo(0, 1, 2), false));
            ammo.add(new AmmoCard(new Ammo(0, 2, 1), false));
            ammo.add(new AmmoCard(new Ammo(1, 0, 2), false));
            ammo.add(new AmmoCard(new Ammo(1, 2, 0), false));
            ammo.add(new AmmoCard(new Ammo(2, 0, 1), false));
            ammo.add(new AmmoCard(new Ammo(2, 1, 0), false));
            ammo.add(new AmmoCard(new Ammo(0, 0, 2), true));
            ammo.add(new AmmoCard(new Ammo(0, 1, 1), true));
            ammo.add(new AmmoCard(new Ammo(0, 2, 0), true));
            ammo.add(new AmmoCard(new Ammo(1, 0, 1), true));
            ammo.add(new AmmoCard(new Ammo(1, 1, 0), true));
            ammo.add(new AmmoCard(new Ammo(2, 0, 0), true));
        }
    }

    /**
     * Generates the default list of weapon cards based on the original rules of the game. Every weapon has a unique
     * function (or more than one) associated to it.
     */
    private static void buildWeapons()
    {
        weapons.add(new WeaponCard("Lock Rifle", new Ammo(1,0,0), new Ammo(2,0,0), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers(1), damage(2).andThen(mark(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(new ShootAction(visiblePlayers(2), damage(2).andThen(mark(1,1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Machine Gun", new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers(2), damage(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1), new Branch(new ShootAction(visiblePlayers(2), damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(new ShootAction(visiblePlayers(3), damage(2,1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1), new Branch(new ShootAction(visiblePlayers(3), damage(2,2,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("THOR", new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(2, 0, 0), new Branch(new ShootAction(thorVisiblePlayers, damage(2,1,2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Plasma Gun", new Ammo(0,0,1), new Ammo(1,0,1), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers(1), damage(2)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers(1), damage(2)), new MoveAction(2), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers(1), damage(3)), new EndBranchAction()),
                        new Branch(new ShootAction(visiblePlayers(1), damage(3)), new MoveAction(2), new EndBranchAction())))));

        weapons.add(new WeaponCard("Whisper", new Ammo(1,0,1), new Ammo(2,0,1), () -> Collections.singletonList(
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(awayPlayers(1, 2), damage(3).andThen(mark(1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Electroscythe", new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageAll(1), nearSquares(1, 0)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 1, 0), new Branch(new ShootAction(damageAll(2), nearSquares(1, 0)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Tractor Beam", new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(tractorBeamPlayers1, tractorBeamSquares1, move.andThen(damage(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 1), new Branch(new ShootAction(tractorBeamPlayers2, tractorBeamSquares2, move.andThen(damage(3))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Vortex Cannon", new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(vortexCannonPlayers1, vortexCannonSquares, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(new ShootAction(vortexCannonPlayers2, vortexCannonSquares, move.andThen(damage(2, 1, 1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Furnace", new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageRoom(1), roomSquares(1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(damageAll(1).andThen(markAll(1)), betweenSquares(1, 1,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Heatseeker", new Ammo(0, 1, 1), new Ammo(0, 2, 1), () -> Collections.singletonList(
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(nonVisiblePlayers(1), damage(3)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Hellion", new Ammo(0, 0, 1), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(awayPlayers(1, 1), damageMultiple(1, 0, 0, 1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0 ,1 ,0), new Branch(new ShootAction(awayPlayers(1, 1), damageMultiple(1, 0, 0, 2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Flamethrower", new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(flamethrowerPlayers, damage(1,1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 2), new Branch(new ShootAction(damageAll(2, 1), flamethrowerSquares), new EndBranchAction())))));

        weapons.add(new WeaponCard("Grenade Launcher", new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0),
                        new Branch(new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new ShootAction(damageAll(1), visibleSquares(1)), new EndBranchAction()),
                        new Branch(new ShootAction(damageAll(1), visibleSquares(1)), new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new EndBranchAction())))));

        weapons.add(new WeaponCard("Rocket Launcher", new Ammo(0, 1, 0), new Ammo(0, 2, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 0),
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1), new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new EndBranchAction())),
                new FireModalityAction(new Ammo(1, 0, 1),
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new MoveAction(2), new EndBranchAction()),
                        new Branch(new MoveAction(2), new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Railgun", new Ammo(1, 0, 1), new Ammo(1, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(railGunPlayers1, damage(3)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(railGunPlayers2, damage(2, 2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Cyberblade", new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0),
                        new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new MoveAction(1), new EndBranchAction()),
                        new Branch(new MoveAction(1), new ShootAction(nearPlayers(1, 0), damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1),
                        new Branch(new ShootAction(nearPlayers(2, 0), damage(2, 2)), new MoveAction(1), new EndBranchAction()),
                        new Branch(new MoveAction(1), new ShootAction(nearPlayers(2, 0), damage(2, 2)), new EndBranchAction()),
                        new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new MoveAction(1, 1), new ShootAction(nearPlayers(1, 0),damage(2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("ZX-2", new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers(1), damage(1).andThen(mark(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(visiblePlayers(3), mark(1, 1, 1), false), new EndBranchAction())))));

        weapons.add(new WeaponCard("Shotgun", new Ammo(0, 0, 1), new Ammo(0, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(nearPlayers(1, 0), movable1Square, move.andThen(damage(3))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(betweenPlayers(1, 1, 1), damage(2)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Power Glove", new Ammo(1, 0, 0), new Ammo(1, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(1, 0), damage(1).andThen(mark(2))), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(flamethrowerPlayers, powerGlove), new EndBranchAction())))));

        weapons.add(new WeaponCard("Shockwave", new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(betweenPlayers(3, 1,1), damage(1, 1, 1)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 0, 1), new Branch(new ShootAction(damageAll(1, 1, 1, 1), betweenSquares(4, 1,1)), new EndBranchAction())))));

        weapons.add(new WeaponCard("Sledgehammer", new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new EndBranchAction())),
                new FireModalityAction(new Ammo(0, 1, 0), new Branch(new ShootAction(nearPlayers(1, 0), sledgehammerSquares, move.andThen(damage(3))), new EndBranchAction())))));
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
    private static ShootFunc powerGlove = (player, players, squares) -> Effects.powerGlove(player, players);
    private static ShootFunc moveSelf = (player, players, squares) -> Effects.move(Collections.singletonList(player), squares);
    private static ShootFunc damageNoMark(Integer ... damage) {return (player, players, squares) -> Effects.damageNoMark(player, players, Arrays.asList(damage)); }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF PARAMETRIZED TARGET FILTERS

    private static PlayersFilter visiblePlayers(int maxTargets) { return (player, players, squares) -> TargetsFilters.visiblePlayers(player, players, maxTargets); }
    private static PlayersFilter awayPlayers(int maxTargets, int minDistance){ return (player, players, squares) -> TargetsFilters.awayPlayers(player, players, maxTargets, minDistance); }
    private static PlayersFilter nearPlayers(int maxTargets, int maxDistance) { return (player, players, squares) -> TargetsFilters.nearPlayers(player, players, maxTargets, maxDistance); }
    private static PlayersFilter betweenPlayers(int maxTargets, int minDistance, int maxDistance) { return (player, players, squares) -> TargetsFilters.betweenPlayers(player, players, maxTargets, minDistance, maxDistance); }
    private static PlayersFilter nonVisiblePlayers(int maxTargets) { return (player, players, squares) -> TargetsFilters.nonVisiblePlayers(player, players, maxTargets); }

    private static SquaresFilter visibleSquares(int maxTargets) { return (player, players, squares) -> TargetsFilters.visibleSquares(player, squares, maxTargets); }
    private static SquaresFilter nearSquares(int maxTargets, int maxDistance) { return (player, players, squares) ->  TargetsFilters.nearSquares(player, squares, maxTargets, maxDistance); }
    private static SquaresFilter betweenSquares(int maxTargets, int minDistance, int maxDistance) { return (player, players, squares) -> TargetsFilters.betweenSquares(player, squares, maxTargets, minDistance, maxDistance); }
    private static SquaresFilter roomSquares(int maxTargets) { return (player, players, squares) -> TargetsFilters.otherVisibleRoom(player, squares, maxTargets); }

    private static SquaresFilter movable1Square = (player, players, squares) -> TargetsFilters.movable1Square(players, squares);

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF SPECIFIC TARGET FILTERS

    private static PlayersFilter thorVisiblePlayers = (player, players, squares) -> TargetsFilters.thorVisiblePlayers(player, players);

    private static PlayersFilter tractorBeamPlayers1 = (player, players, squares) -> TargetsFilters.tractorBeamPlayers1(player, players);
    private static SquaresFilter tractorBeamSquares1 = TargetsFilters::tractorBeamSquares1;
    private static PlayersFilter tractorBeamPlayers2 = (player, players, squares) -> TargetsFilters.tractorBeamPlayers2(player, players);
    private static SquaresFilter tractorBeamSquares2 = TargetsFilters::tractorBeamSquares2;

    private static SquaresFilter vortexCannonSquares = (player, players, squares) -> TargetsFilters.vortexCannonSquares(player, squares);
    private static PlayersFilter vortexCannonPlayers1 = TargetsFilters::vortexCannonPlayers1;
    private static PlayersFilter vortexCannonPlayers2 = TargetsFilters::vortexCannonPlayers2;

    private static PlayersFilter flamethrowerPlayers = (player, players, squares) -> TargetsFilters.flamethrowerPlayers(player, players);
    private static SquaresFilter flamethrowerSquares = (player, players, squares) -> TargetsFilters.flamethrowerSquares(player, squares);

    private static PlayersFilter railGunPlayers1 = (player, players, squares) -> TargetsFilters.railgunPlayers1(player, players);
    private static PlayersFilter railGunPlayers2 = (player, players, squares) -> TargetsFilters.railgunPlayers2(player, players);

    private static SquaresFilter sledgehammerSquares = (player, players, squares) -> TargetsFilters.sledgehammerSquares(players, squares);

    //------------------------------------------------------------------------------------------------------------------
    // POWER UP'S TARGET FILTERS

    private static PlayersFilter otherPlayers = (player, players, squares) -> TargetsFilters.newtonPlayers(player, players);
    private static SquaresFilter near2Squares = (player, players, squares) -> TargetsFilters.newtonSquares(players, squares);

    private static SquaresFilter allSquares = (player, players, squares) -> TargetsFilters.teleporterSquares(player, squares);

}
