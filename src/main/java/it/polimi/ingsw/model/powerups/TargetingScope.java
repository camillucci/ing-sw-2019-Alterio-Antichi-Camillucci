package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;

public class TargetingScope extends PowerUpCard {

    public TargetingScope(AmmoColor color) {
        this.name = "Targeting Scope";
        this.color = color;
    }

    @Override
    public void visualize(){
        //TODO
    }

    @Override
    protected void buildFireModality(Player shooter) {
        //TODO
    }
}
