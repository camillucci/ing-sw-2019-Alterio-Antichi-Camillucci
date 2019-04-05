package it.polimi.ingsw.model;

public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    public AmmoSquare(SquareBorder north, SquareBorder sud, SquareBorder west, SquareBorder est, AmmoCard ammoCard) {
        this.north = north;
        this.sud = sud;
        this.west = west;
        this.est = est;
        this.ammoCard = ammoCard;
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
