package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.AmmoColor.*;

public class PowerUpFactory {

    private PowerUpFactory(){}

    private static ArrayList<PowerUpCard> powerUpCards = new ArrayList<>();

    public static List<PowerUpCard> getPowerUps()
    {
        buildPowerUps();
        return new ArrayList<>(powerUpCards);
    }

    private static void buildPowerUps() {
        powerUpCards.clear();
        for(int i = 0; i < 24; i++) {
            //powerUpCards.add(new PowerUpCard("Newton",BLUE, new Ammo(0, 0, 0)));
            //TODO
        }
    }
}
