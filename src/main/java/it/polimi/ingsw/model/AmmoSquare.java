package it.polimi.ingsw.model;

import java.util.ArrayList;

public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    public AmmoSquare(int x, int y, SquareBorder[] borders, AmmoCard ammoCard) {
        this.x = x;
        this.y = y;
        this.north = borders[0];
        this.south = borders[1];
        this.west = borders[2];
        this.east = borders[3];
        this.ammoCard = ammoCard;
        this.players = new ArrayList<>();
    }

    @Override
    public void grab(Player player) {
        if(!this.isEmpty()) {
            player.addRed(ammoCard.getRed());
            player.addBlue(ammoCard.getBlue());
            player.addYellow(ammoCard.getYellow());

            if (ammoCard.isPowerUpCard()) {
                player.addPowerUpCard();
            }
            ammoCard = null;
        }
    }

    public boolean isEmpty() {
        return ammoCard == null;
    }

    public void setAmmoCard(AmmoCard ammoCard) {
        this.ammoCard = ammoCard;
    }
}
