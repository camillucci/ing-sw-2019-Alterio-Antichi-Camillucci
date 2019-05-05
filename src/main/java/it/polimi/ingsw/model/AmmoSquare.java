package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    public AmmoSquare(int y, int x, SquareBorder[] borders, AmmoCard ammoCard) {
        this.y = y;
        this.x = x;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
        this.ammoCard = ammoCard;
        this.players = new ArrayList<>();
    }

    @Override
    public List<Branch> grab(Player player) {
        if(!this.isEmpty()) {
            player.addRed(ammoCard.getRed());
            player.addBlue(ammoCard.getBlue());
            player.addYellow(ammoCard.getYellow());

            if (ammoCard.isPowerUpCard()) {
                player.addPowerUpCard();
            }
            ammoCard = null;
        }
        return Collections.emptyList();
    }

    public boolean isEmpty() {
        return ammoCard == null;
    }

    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }

    @Override
    public List<String> getCardsName() {
        return Collections.singletonList(ammoCard.getName());
    }
}
