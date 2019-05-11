package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Effects
{
    private Effects(){}

    public static void damage(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++)
            if(damage.get(i) > 0)
                targets.get(i).addDamage(shooter, damage.get(i));
    }

    public static void mark(Player shooter, List<Player> targets, List<Integer> marks)
    {
        for(int i = 0; i < targets.size(); i++)
            if(marks.get(i) > 0)
                targets.get(i).addMark(shooter, marks.get(i));
    }

    public static void damageAll(Player shooter, List<Square> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++)
            if(damage.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                for(Player p : temp)
                    p.addDamage(shooter, damage.get(i));
            }
    }

    public static void markAll(Player shooter, List<Square> targets, List<Integer> marks)
    {
        for(int i = 0; i < targets.size(); i++)
            if(marks.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                for(Player p : temp)
                    p.addMark(shooter, marks.get(i));
            }
    }

    public static void damageRoom(Player shooter, List<Square> targets, List<Integer> damage)
    {
        for(int i = 0; i < targets.size(); i++) {
            List<Integer> temp = Arrays.asList(damage.get(i), damage.get(i), damage.get(i), damage.get(i));
            damageAll(shooter, shooter.gameBoard.getRoom(targets.get(i)), temp);
        }
    }

    public static void damageMultiple(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(Player target : targets) {
            damage(shooter, Collections.singletonList(target), Collections.singletonList(damage.get(0)));
            damageAll(shooter, Collections.singletonList(target.getCurrentSquare()), Collections.singletonList(damage.get(1)));
            mark(shooter, Collections.singletonList(target), Collections.singletonList(damage.get(2)));
            markAll(shooter, Collections.singletonList(target.getCurrentSquare()), Collections.singletonList(damage.get(3)));
        }
    }

    public static void move(List<Player> players, List<Square> squares)
    {
        for(Player player : players) {
            player.getCurrentSquare().removePlayer(player);
            player.setCurrentSquare(squares.get(0));
            squares.get(0).addPlayer(player);
        }
    }

    public static void powerGlove(Player shooter, List<Player> targets) {
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
}
