package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class powerUpSetTest
{
    @Test
    void test()
    {
        PowerupSet set = new PowerupSet();
        PowerUpCard powerup1 = new EndStartPowerUpCard("lala", AmmoColor.BLUE, new Ammo(0,0,0), () -> new PowerUpAction());
        PowerUpCard powerup2 = new EndStartPowerUpCard("lala2", AmmoColor.BLUE, new Ammo(0,0,0), () -> new PowerUpAction());
        set.add(powerup1);
        set.add(powerup2);
        assertTrue(set.getEndStartPUs().size() == 2);
        set.remove(powerup1);
        set.remove(powerup2);
        assertTrue(set.getEndStartPUs().isEmpty());
    }
}
