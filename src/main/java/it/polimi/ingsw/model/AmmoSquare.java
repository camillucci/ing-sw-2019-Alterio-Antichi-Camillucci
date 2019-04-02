package it.polimi.ingsw.model;

public class AmmoSquare extends Square {

    private AmmoCard ammoCard;

    public AmmoSquare() {
        //TODO
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
