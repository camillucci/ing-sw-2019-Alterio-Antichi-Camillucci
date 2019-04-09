package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;

public class Teleporter extends PowerUpCard {

    public Teleporter(AmmoColor color) {
        this.name = "Teleporter";
        this.color = color;
    }

    @Override
    public void visualize(){}

    @Override
    protected void buildFireModalities(Player shooter) {

    }
}
