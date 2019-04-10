package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.AmmoColor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PowerUpCard;

public class TagbackGrenade extends PowerUpCard {

    public TagbackGrenade(AmmoColor color) {
        this.name = "Tagback Grenade";
        this.color = color;
    }

    @Override
    public void visualize(){
        //TODO
    }

    @Override
    protected void buildFireModality() {

    }

}
