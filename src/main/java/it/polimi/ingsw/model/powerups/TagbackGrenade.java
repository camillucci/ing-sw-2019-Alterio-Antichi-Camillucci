package it.polimi.ingsw.model.powerups;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.action.ShootAction;

import java.util.List;

public class TagbackGrenade extends PowerUpCard {

    public TagbackGrenade(AmmoColor color) {
        this.name = "Tagback Grenade";
        this.color = color;
    }

    @Override
    protected void buildFireModality(Player shooter){
        ShootAction s1 = new ShootAction(this :: effect, null, shooter);
    }

    private void effect(Player shooter, List<Player> target){
        target.get(0).addMark(shooter);
    }
}
