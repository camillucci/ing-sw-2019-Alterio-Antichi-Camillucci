package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.PowerUpAction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpSetTest
{
    @Test
    void test()
    {
        PowerupSet set = new PowerupSet();
        PowerUpCard powerup1 = new EndStartPowerUpCard("lala", AmmoColor.BLUE, new Ammo(0,0,0), PowerUpAction::new);
        PowerUpCard powerup2 = new EndStartPowerUpCard("lala2", AmmoColor.BLUE, new Ammo(0,0,0), PowerUpAction::new);
        set.add(powerup1);
        set.add(powerup2);
        assertEquals(2, set.getEndStartPUs().size());
        set.remove(powerup1);
        set.remove(powerup2);
        assertTrue(set.getEndStartPUs().isEmpty());
    }
}
