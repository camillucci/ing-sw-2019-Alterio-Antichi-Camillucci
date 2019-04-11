package it.polimi.ingsw.model.weapons;

import it.polimi.ingsw.model.Player;
import java.util.List;

public class Effects
{
    private Effects(){}
    public static void damage(Player shooter, List<Player> targets, List<Integer> damage)
    {
        for(int i=0; i < targets.size(); i++)
            if(damage.get(i) > 0)
                targets.get(0).addDamage(shooter, damage.get(i));
    }

    public static void mark(Player shooter, List<Player> targets, List<Integer> marks)
    {
        for(int i=0; i < targets.size(); i++)
            if(marks.get(i) > 0)
                targets.get(0).addMark(shooter, marks.get(i));
    }
}
