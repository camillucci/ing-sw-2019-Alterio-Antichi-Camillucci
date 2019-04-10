package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.PowerUpCard;

public class Newton extends PowerUpCard {

    public Newton(AmmoColor color) {
        this.name = "Newton";
        this.color = color;
    }

    @Override
    public void visualize(){
        //TODO
    }

    @Override
    protected void buildFireModality() {
        //TODO
    }
}
