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
        //TODO
    }

    public boolean isEmpty() {
        if(ammoCard == null) return true;
        else return false;
    }
}
