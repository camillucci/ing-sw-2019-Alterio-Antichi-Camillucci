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
            powerUpCards.add(new EndStartPowerUpCard("Teleporter", ammoColor, TargetsFilters.noPlayersFilter, allSquares, moveSelf));
            powerUpCards.add(new InTurnPowerUpCard("Targeting Scope", ammoColor, damageNoMark(1)));
            powerUpCards.add(new EndStartPowerUpCard("Newton", ammoColor, otherPlayers, near2Squares, move));
            powerUpCards.add(new CounterAttackPowerUpCard("Tagback Grenade", ammoColor, mark(1)));
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
        weapons.add(new WeaponCard(LOCK_RIFLE, new Ammo(1,0,0), new Ammo(2,0,0), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0), LOCK_RIFLE, BASIC_MODE,
                        new Branch(new ShootAction(visiblePlayers(1), damage(2).andThen(mark(1))), new EndBranchAction(LOCK_RIFLE))),
                new FireModalityAction(new Ammo(0, 1, 0), LOCK_RIFLE, "Second Lock",
                        new Branch(new ShootAction(visiblePlayers(2), damage(2).andThen(mark(1,1))), new EndBranchAction(LOCK_RIFLE))))));

        weapons.add(new WeaponCard(MACHINE_GUN, new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), MACHINE_GUN, BASIC_MODE,
                        new Branch(new ShootAction(visiblePlayers(2), damage(1)), new EndBranchAction(MACHINE_GUN))),
                new FireModalityAction(new Ammo(0, 0, 1), MACHINE_GUN, "Focus Shot",
                        new Branch(new ShootAction(visiblePlayers(2), damage(2,1)), new EndBranchAction(MACHINE_GUN))),
                new FireModalityAction(new Ammo(1, 0, 0), MACHINE_GUN, "Turret Tripod",
                        new Branch(new ShootAction(visiblePlayers(3), damage(2,1,1)), new EndBranchAction(MACHINE_GUN))),
                new FireModalityAction(new Ammo(1, 0, 1), MACHINE_GUN, "Focus Shot and Turret Tripod",
                        new Branch(new ShootAction(visiblePlayers(3), damage(2,2,1)), new EndBranchAction(MACHINE_GUN))))));

        weapons.add(new WeaponCard(THOR, new Ammo(0,1,0), new Ammo(1,1,0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), THOR, BASIC_MODE,
                        new Branch(new ShootAction(thorVisiblePlayers, damage(2)), new EndBranchAction(THOR))),
                new FireModalityAction(new Ammo(1, 0, 0), THOR, "Chain Reaction",
                        new Branch(new ShootAction(thorVisiblePlayers, damage(2,1)), new EndBranchAction(THOR))),
                new FireModalityAction(new Ammo(2, 0, 0), THOR, "High Voltage",
                        new Branch(new ShootAction(thorVisiblePlayers, damage(2,1,2)), new EndBranchAction(THOR))))));

        weapons.add(new WeaponCard(PLASMA_GUN, new Ammo(0,0,1), new Ammo(1,0,1), () -> Arrays.asList(
                new FireModalityAction(new Ammo(0, 0, 0), PLASMA_GUN, "Phase Glide",
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers(1), damage(2)), new EndBranchAction(PLASMA_GUN)),
                        new Branch(new ShootAction(visiblePlayers(1), damage(2)), new MoveAction(2), new EndBranchAction(PLASMA_GUN))),
                new FireModalityAction(new Ammo(1, 0, 0), PLASMA_GUN, "Charged Shot",
                        new Branch(new MoveAction(2), new ShootAction(visiblePlayers(1), damage(3)), new EndBranchAction(PLASMA_GUN)),
                        new Branch(new ShootAction(visiblePlayers(1), damage(3)), new MoveAction(2), new EndBranchAction(PLASMA_GUN))))));

        weapons.add(new WeaponCard(WHISPER, new Ammo(1,0,1), new Ammo(2,0,1), () -> Collections.singletonList(
                new FireModalityAction(new Ammo(0, 0, 0), WHISPER, BASIC_MODE,
                        new Branch(new ShootAction(awayPlayers(1, 2), damage(3).andThen(mark(1))), new EndBranchAction(WHISPER))))));

        weapons.add(new WeaponCard(ELECTROSCYTHE, new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), ELECTROSCYTHE, BASIC_MODE,
                        new Branch(new ShootAction(damageAll(1), nearSquares(1, 0)), new EndBranchAction(ELECTROSCYTHE))),
                new FireModalityAction(new Ammo(1, 1, 0), ELECTROSCYTHE, "Reaper Mode",
                        new Branch(new ShootAction(damageAll(2), nearSquares(1, 0)), new EndBranchAction(ELECTROSCYTHE))))));

        weapons.add(new WeaponCard(TRACTOR_BEAM, new Ammo(0, 0, 0), new Ammo(1, 0, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), TRACTOR_BEAM, BASIC_MODE,
                        new Branch(new ShootAction(tractorBeamPlayers1, tractorBeamSquares1, move.andThen(damage(1))), new EndBranchAction(TRACTOR_BEAM))),
                new FireModalityAction(new Ammo(0, 1, 1), TRACTOR_BEAM, "Punisher Mode",
                        new Branch(new ShootAction(tractorBeamPlayers2, tractorBeamSquares2, move.andThen(damage(3))), new EndBranchAction(TRACTOR_BEAM))))));

        weapons.add(new WeaponCard(VORTEX_CANNON, new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), VORTEX_CANNON, BASIC_MODE,
                        new Branch(new ShootAction(vortexCannonPlayers1, vortexCannonSquares, move.andThen(damage(2))), new EndBranchAction(VORTEX_CANNON))),
                new FireModalityAction(new Ammo(0, 1, 0), VORTEX_CANNON, "Black Hole",
                        new Branch(new ShootAction(vortexCannonPlayers2, vortexCannonSquares, move.andThen(damage(2, 1, 1))), new EndBranchAction(VORTEX_CANNON))))));

        weapons.add(new WeaponCard(FURNACE, new Ammo(1, 0, 0), new Ammo(1, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), FURNACE, BASIC_MODE,
                        new Branch(new ShootAction(damageRoom(1), roomSquares(1)), new EndBranchAction(FURNACE))),
                new FireModalityAction(new Ammo(0, 0, 0), FURNACE, "Cozy Fire Mode",
                        new Branch(new ShootAction(damageAll(1).andThen(markAll(1)), betweenSquares(1, 1,1)), new EndBranchAction(FURNACE))))));

        weapons.add(new WeaponCard(HEATSEEKER, new Ammo(0, 1, 1), new Ammo(0, 2, 1), () -> Collections.singletonList(
                new FireModalityAction(new Ammo(0, 0, 0), HEATSEEKER, BASIC_MODE,
                        new Branch(new ShootAction(nonVisiblePlayers(1), damage(3)), new EndBranchAction(HEATSEEKER))))));

        weapons.add(new WeaponCard(HELLION, new Ammo(0, 0, 1), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), HELLION, BASIC_MODE,
                        new Branch(new ShootAction(awayPlayers(1, 1), damageMultiple(1, 0, 0, 1)), new EndBranchAction(HELLION))),
                new FireModalityAction(new Ammo(0 ,1 ,0),HELLION, "Nano-Tracer Mode",
                        new Branch(new ShootAction(awayPlayers(1, 1), damageMultiple(1, 0, 0, 2)), new EndBranchAction(HELLION))))));

        weapons.add(new WeaponCard(FLAMETHROWER, new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), FLAMETHROWER, BASIC_MODE,
                        new Branch(new ShootAction(flamethrowerPlayers, damage(1,1)), new EndBranchAction(FLAMETHROWER))),
                new FireModalityAction(new Ammo(0, 0, 2), FLAMETHROWER, "Barbecue Mode",
                        new Branch(new ShootAction(damageAll(2, 1), flamethrowerSquares), new EndBranchAction(FLAMETHROWER))))));

        weapons.add(new WeaponCard(GRENADE_LAUNCHER, new Ammo(0, 0, 0), new Ammo(0, 1, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), GRENADE_LAUNCHER, BASIC_MODE,
                        new Branch(new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new EndBranchAction(GRENADE_LAUNCHER))),
                new FireModalityAction(new Ammo(0, 1, 0), GRENADE_LAUNCHER, "Extra Grenade",
                        new Branch(new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new ShootAction(damageAll(1), visibleSquares(1)), new EndBranchAction(GRENADE_LAUNCHER)),
                        new Branch(new ShootAction(damageAll(1), visibleSquares(1)), new ShootAction(visiblePlayers(1), movable1Square, move.andThen(damage(1))), new EndBranchAction(GRENADE_LAUNCHER))))));

        weapons.add(new WeaponCard(ROCKET_LAUNCHER, new Ammo(0, 1, 0), new Ammo(0, 2, 0), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), ROCKET_LAUNCHER, BASIC_MODE,
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new EndBranchAction(ROCKET_LAUNCHER))),
                new FireModalityAction(new Ammo(1, 0, 0), ROCKET_LAUNCHER, "Rocket Jump",
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new MoveAction(2), new EndBranchAction(ROCKET_LAUNCHER)),
                        new Branch(new MoveAction(2), new ShootAction(awayPlayers(1, 1), movable1Square, move.andThen(damage(2))), new EndBranchAction(ROCKET_LAUNCHER))),
                new FireModalityAction(new Ammo(0, 0, 1), ROCKET_LAUNCHER, "Fragmenting Warhead",
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new EndBranchAction(ROCKET_LAUNCHER))),
                new FireModalityAction(new Ammo(1, 0, 1), ROCKET_LAUNCHER, "Rocket Jump and Fragmenting Warhead",
                        new Branch(new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new MoveAction(2), new EndBranchAction(ROCKET_LAUNCHER)),
                        new Branch(new MoveAction(2), new ShootAction(awayPlayers(1, 1), movable1Square, damageMultiple(2, 1, 0, 0).andThen(move)), new EndBranchAction(ROCKET_LAUNCHER))))));

        weapons.add(new WeaponCard(RAILGUN, new Ammo(1, 0, 1), new Ammo(1, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), RAILGUN, BASIC_MODE,
                        new Branch(new ShootAction(railGunPlayers1, damage(3)), new EndBranchAction(RAILGUN))),
                new FireModalityAction(new Ammo(0, 0, 0), RAILGUN, "Piercing Mode",
                        new Branch(new ShootAction(railGunPlayers2, damage(2, 2)), new EndBranchAction(RAILGUN))))));

        weapons.add(new WeaponCard(CYBERBLADE, new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), CYBERBLADE, "Shadowstep",
                        new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new MoveAction(1), new EndBranchAction(CYBERBLADE)),
                        new Branch(new MoveAction(1), new ShootAction(nearPlayers(1, 0), damage(2)), new EndBranchAction(CYBERBLADE))),
                new FireModalityAction(new Ammo(0, 0, 1), CYBERBLADE, "Slice and Dice",
                        new Branch(new ShootAction(nearPlayers(2, 0), damage(2, 2)), new MoveAction(1), new EndBranchAction(CYBERBLADE)),
                        new Branch(new MoveAction(1), new ShootAction(nearPlayers(2, 0), damage(2, 2)), new EndBranchAction(CYBERBLADE)),
                        new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new MoveAction(1, 1), new ShootAction(nearPlayers(1, 0),damage(2)), new EndBranchAction(CYBERBLADE))))));

        weapons.add(new WeaponCard(ZX_2, new Ammo(0, 1, 0), new Ammo(0, 1, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), ZX_2, BASIC_MODE,
                        new Branch(new ShootAction(visiblePlayers(1), damage(1).andThen(mark(2))), new EndBranchAction(ZX_2))),
                new FireModalityAction(new Ammo(0, 0, 0), ZX_2, "Scanner Mode",
                        new Branch(new ShootAction(visiblePlayers(3), mark(1, 1, 1), false), new EndBranchAction(ZX_2))))));

        weapons.add(new WeaponCard(SHOTGUN, new Ammo(0, 0, 1), new Ammo(0, 0, 2), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), SHOTGUN, BASIC_MODE,
                        new Branch(new ShootAction(nearPlayers(1, 0), movable1Square, move.andThen(damage(3))), new EndBranchAction(SHOTGUN))),
                new FireModalityAction(new Ammo(0, 0, 0), SHOTGUN, "Long Barrel Mode",
                        new Branch(new ShootAction(betweenPlayers(1, 1, 1), damage(2)), new EndBranchAction(SHOTGUN))))));

        weapons.add(new WeaponCard(POWER_GLOVE, new Ammo(1, 0, 0), new Ammo(1, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), POWER_GLOVE, BASIC_MODE,
                        new Branch(new MoveAction(1, 1), new ShootAction(nearPlayers(1, 0), damage(1).andThen(mark(2))), new EndBranchAction(POWER_GLOVE))),
                new FireModalityAction(new Ammo(0, 0, 0), POWER_GLOVE, "Rocket Fist Mode",
                        new Branch(new ShootAction(flamethrowerPlayers, powerGloveEffect), new EndBranchAction(POWER_GLOVE))))));

        weapons.add(new WeaponCard(SHOCKWAVE, new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), SHOCKWAVE, BASIC_MODE,
                        new Branch(new ShootAction(betweenPlayers(3, 1,1), damage(1, 1, 1)), new EndBranchAction(SHOCKWAVE))),
                new FireModalityAction(new Ammo(0, 0, 1), SHOCKWAVE, "Tsunami Mode",
                        new Branch(new ShootAction(damageAll(1, 1, 1, 1), betweenSquares(4, 1,1)), new EndBranchAction(SHOCKWAVE))))));

        weapons.add(new WeaponCard(SLEDGEHAMMER, new Ammo(0, 0, 0), new Ammo(0, 0, 1), () -> Arrays.asList (
                new FireModalityAction(new Ammo(0, 0, 0), SLEDGEHAMMER, BASIC_MODE,
                        new Branch(new ShootAction(nearPlayers(1, 0), damage(2)), new EndBranchAction(SLEDGEHAMMER))),
                new FireModalityAction(new Ammo(0, 1, 0), SLEDGEHAMMER, "Pulverize Mode",
                        new Branch(new ShootAction(nearPlayers(1, 0), sledgehammerSquares, move.andThen(damage(3))), new EndBranchAction(SLEDGEHAMMER))))));
    }

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF NAMES
    private static final String LOCK_RIFLE = "Lock Rifle";
    private static final String MACHINE_GUN = "Machine Gun";
    private static final String THOR = "THOR";
    private static final String PLASMA_GUN = "Plasma Gun";
    private static final String WHISPER = "Whisper";
    private static final String ELECTROSCYTHE = "Electroscythe";
    private static final String TRACTOR_BEAM = "Tractor Beam";
    private static final String VORTEX_CANNON = "Vortex Cannon";
    private static final String FURNACE = "Furnace";
    private static final String HEATSEEKER = "Heatseeker";
    private static final String HELLION = "Hellion";
    private static final String FLAMETHROWER = "Flamethrower";
    private static final String GRENADE_LAUNCHER = "Grenade Launcher";
    private static final String ROCKET_LAUNCHER = "Rocket Launcher";
    private static final String RAILGUN = "Railgun";
    private static final String CYBERBLADE = "Cyberblade";
    private static final String ZX_2 = "ZX-2";
    private static final String SHOTGUN = "Shotgun";
    private static final String POWER_GLOVE = "Power Glove";
    private static final String SHOCKWAVE = "Shockwave";
    private static final String SLEDGEHAMMER = "Sledgehammer";

    private static final String BASIC_MODE = "Basic Mode";

    //------------------------------------------------------------------------------------------------------------------
    // LIST OF EFFECTS

    private static ShootFunc damage(Integer ... damage) { return (player, players, squares) -> Effects.damage(player, players, Arrays.asList(damage)); }
    private static ShootFunc mark(Integer ... marks) { return (player, players, squares) -> Effects.mark(player, players, Arrays.asList(marks)); }
    private static ShootFunc damageAll(Integer ... damage) { return (player, players, squares) -> Effects.damageAll(player, squares, Arrays.asList(damage)); }
    private static ShootFunc markAll(Integer ... marks) { return (player, players, squares) -> Effects.markAll(player, squares, Arrays.asList(marks)); }
    private static ShootFunc damageRoom (Integer ... damage) { return (player, players, squares) -> Effects.damageRoom(player, squares, Arrays.asList(damage)); }
    private static ShootFunc damageMultiple(Integer ... damage) { return (player, players, squares) -> Effects.damageMultiple(player, players, Arrays.asList(damage)); }
    private static ShootFunc move = (player, players, squares) -> Effects.move(players, squares);
    private static ShootFunc powerGloveEffect = (player, players, squares) -> Effects.powerGloveEffect(player, players);
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
