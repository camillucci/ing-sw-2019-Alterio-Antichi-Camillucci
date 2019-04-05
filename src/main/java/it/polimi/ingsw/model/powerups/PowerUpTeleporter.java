package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpCard;

public class PowerUpTeleporter extends PowerUpCard {

    public PowerUpTeleporter(AmmoColor color) {
        this.name = "Teleporter";
        this.color = color;
    }
}
