package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class features a width number of public static methods that apply effects to the board state and the players.
 * Those methods are all called when a wepon applies one of the affects listed below or when a power up card does.
 */
public class Effects
{
    private Effects(){}

    /**
     * Effect that adds damage drops to a player or more
     * @param shooter The player that deals the damage and from whom is taken the color of the damage drops
     * @param targets The players who receive the damage drops
     * @param damage A list of integers, each of who represent the amount of damage drops that need to be added to
     *               the corresponding player (the correspondence is based on both the integer's and the target's
     *               index in their respective lists)
     */
    public static void damage(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++)
            if(damage.size() > i && damage.get(i) > 0)
                targets.get(i).addDamage(shooter, damage.get(i));
    }

    /**
     * Effect that adds marks to a player or more
     * @param shooter The player that applies the marks and from whom is taken the color of said marks
     * @param targets The players who receive the marks
     * @param marks A list of integers, each of who represent the amount of marks that need to be added to
     *               the corresponding player (the correspondence is based on both the integer's and the target's
     *               index in their respective lists)
     */
    public static void mark(Player shooter, List<Player> targets, List<Integer> marks)
    {
        for(int i = 0; i < targets.size(); i++)
            if(marks.size() > i && marks.get(i) > 0)
                targets.get(i).addMark(shooter, marks.get(i));
    }

    /**
     * Effect that damages all players standing in one or more squares
     * @param shooter The player that deals the damage and from whom is taken the color of the damage drops
     * @param targets The squares that are being shot
     * @param damage A list of integers, each of who represent the amount of damage drops that need to be added to
     *                the players in the corresponding square (the correspondence is based on both the integer's and the target's
     *                index in their respective lists)
     */
    public static void damageAll(Player shooter, List<Square> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++)
            if(damage.size() > i && damage.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                temp.remove(shooter);
                for(Player p : temp)
                    p.addDamage(shooter, damage.get(i));
            }
    }

    /**
     * Effect that applies marks to all players standing in one or more squares
     * @param shooter The player that applies the marks and from whom is taken the color of the damage drops
     * @param targets The squares that are being shot
     * @param marks A list of integers, each of who represent the amount of marks that need to be added to
     *                the players in the corresponding square (the correspondence is based on both the integer's and the target's
     *                index in their respective lists)
     */
    public static void markAll(Player shooter, List<Square> targets, List<Integer> marks)
    {
        for(int i = 0; i < targets.size(); i++)
            if(marks.size() > i && marks.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                temp.remove(shooter);
                for(Player p : temp)
                    p.addMark(shooter, marks.get(i));
            }
    }

    /**
     * Effect that damages all the players standing in the same room
     * @param shooter The player shooting and from whom is taken the color of the damage drops
     * @param targets The squares that are going to be damaged by the effect
     * @param damage A list of integers, each of who represent the amount of damage applied to their corresponding
     *               target. The correspondence is based on both the integer's and target's index in their respective
     *               lists
     */
    public static void damageRoom(Player shooter, List<Square> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++) {
            List<Integer> temp = Arrays.asList(damage.get(i), damage.get(i), damage.get(i), damage.get(i));
            damageAll(shooter, shooter.gameBoard.getRoom(targets.get(i)), temp);
        }
    }

    /**
     * Specific effect that deals damage to every player in the target list and then deals damage to every other player
     * standing in the square of the target. Subsequently, every target is marked and every other player standing in
     * the same square of the target is marked as well.
     * @param shooter Player who is shooting. The color of the damage drops and marks applied to the targets is based
     *                on the shooter.
     * @param targets List of players who receive the initial damage and marks.
     * @param damage List of integers with different meanings. Each integer represents the amount of damage/marks that
     *               needs to be applied to the targets, based on the stage of the effect that we're in.
     */
    public static void damageMultiple(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(Player target : targets) {
            damage(shooter, Collections.singletonList(target), Collections.singletonList(damage.get(0)));
            damageAll(shooter, Collections.singletonList(target.getCurrentSquare()), Collections.singletonList(damage.get(1)));
            mark(shooter, Collections.singletonList(target), Collections.singletonList(damage.get(2)));
            markAll(shooter, Collections.singletonList(target.getCurrentSquare()), Collections.singletonList(damage.get(3)));
        }
    }

    /**
     * Effect that moves one or more players from a square to another (if there is more than one target, the players
     * don't necessarily need to be moved to the same square
     * @param players List of players who are going to be moved away from their current squares
     * @param squares List of squares that the players are going to be moved to. Each player is moved to the square
     *                that has the same index.
     */
    public static void move(List<Player> players, List<Square> squares)
    {
        for(Player player : players) {
            player.getCurrentSquare().removePlayer(player);
            player.setCurrentSquare(squares.get(0));
            squares.get(0).addPlayer(player);
        }
    }

    /**
     * Effect that applies the basic damage effect by calling the damage method. Then, if there is only one target,
     * that player is moved to the square the shooter is standing in. Otherwise, either the first or second player of
     * the targets list is moved to the square the shooter is standing in. The damage applied to every target is always
     * equal to 2 damage drops.
     * @param shooter The player who is shooting and from whom is taken the color of the damage drops added to the
     *                targets.
     * @param targets A list of players that represents the players that are being damaged.
     */
    public static void powerGloveEffect(Player shooter, List<Player> targets) {
        damage(shooter, targets, Arrays.asList(2, 2));
        if(targets.size() == 1)
            move(Collections.singletonList(shooter), Collections.singletonList(targets.get(0).getCurrentSquare()));
        else
            if(Math.abs(shooter.getCurrentSquare().x - targets.get(0).getCurrentSquare().x) == 2
                    || Math.abs(shooter.getCurrentSquare().y - targets.get(0).getCurrentSquare().y) == 2)
                move(Collections.singletonList(shooter), Collections.singletonList(targets.get(0).getCurrentSquare()));
            else
                move(Collections.singletonList(shooter), Collections.singletonList(targets.get(1).getCurrentSquare()));
    }

    /**
     * Effect that deals damage to players without activating the marks
     * @param shooter Player who is shooting. The color of the damage drops and marks applied to the targets is based
     *      *                on the shooter.
     * @param targets A list of players that represents the players that are being damaged.
     * @param damage A list of integers, each of who represent the amount of damage drops that need to be added to
     *      *               the corresponding player (the correspondence is based on both the integer's and the target's
     *      *               index in their respective lists)
     */
    public static void damageNoMark(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++)
            if(damage.size() > i && damage.get(i) > 0)
                targets.get(i).addDamageNoMarks(shooter, damage.get(i));
    }
}
