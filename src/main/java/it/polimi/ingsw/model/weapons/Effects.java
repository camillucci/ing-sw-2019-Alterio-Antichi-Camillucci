package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Square;

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
}
