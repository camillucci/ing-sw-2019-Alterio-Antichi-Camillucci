package it.polimi.ingsw.model;

public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    public AmmoSquare(SquareBorder nord, SquareBorder sud, SquareBorder west, SquareBorder est, AmmoCard ammoCard) {
        this.nord = nord;
        this.sud = sud;
        this.west = west;
        this.est = est;
        this.ammoCard = ammoCard;
    }

    @Override
    public void grab(Player player) {
        player.addRed(ammoCard.getRed());
        player.addBlue(ammoCard.getBlue());
        player.addYellow(ammoCard.getYellow());

        if(ammoCard.isPowerUpCard() == true) {

            player.addPowerUpCard();
        }



    }

    public boolean isEmpty() {
        return ammoCard == null;
    }
}
