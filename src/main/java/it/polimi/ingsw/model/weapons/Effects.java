package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Effects
{
    private Effects(){}

    public static void damage(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(int i=0; i < targets.size(); i++)
            if(damage.get(i) > 0)
                targets.get(i).addDamage(shooter, damage.get(i));
    }

    public static void mark(Player shooter, List<Player> targets, List<Integer> marks)
    {
        for(int i=0; i < targets.size(); i++)
            if(marks.get(i) > 0)
                targets.get(i).addMark(shooter, marks.get(i));
    }

    public static void damageAll(Player shooter, List<Square> targets, List<Integer> damage)
    {
        for(int i=0; i < targets.size(); i++)
            if(damage.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                for(Player p : temp)
                    p.addDamage(shooter, damage.get(i));
            }
    }

    public static void markAll(Player shooter, List<Square> targets, List<Integer> marks)
    {
        for(int i=0; i < targets.size(); i++)
            if(marks.get(i) > 0) {
                List<Player> temp = targets.get(i).getPlayers();
                for(Player p : temp)
                    p.addMark(shooter, marks.get(i));
            }
    }

    public static void damageRoom(Player shooter, List<Square> targets, List<Integer> damage) {
        for (int i = 0; i < targets.size(); i++) {
            List<Integer> temp = new ArrayList<>(Arrays.asList(damage.get(i), damage.get(i), damage.get(i), damage.get(i)));
            damageAll(shooter, shooter.getGameBoard().getRoom(targets.get(i)), temp);
        }
    }

    public static void damageHellion(Player shooter, List<Player> targets, List<Integer> damage)
    {
        damage(shooter, Arrays.asList(targets.get(0)), Arrays.asList(damage.get(0)));
        mark(shooter, Arrays.asList(targets.get(0)), Arrays.asList(damage.get(1)));
        damageAll(shooter, Arrays.asList(targets.get(0).getCurrentSquare()), Arrays.asList(damage.get(2)));
        markAll(shooter, Arrays.asList(targets.get(0).getCurrentSquare()), Arrays.asList(damage.get(3)));
    }
}
