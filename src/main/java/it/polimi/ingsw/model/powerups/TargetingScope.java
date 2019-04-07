package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpCard;

public class TargetingScope extends PowerUpCard {

    public TargetingScope(AmmoColor color) {
        this.name = "Targeting Scope";
        this.color = color;
    }
}
