package it.polimi.ingsw.model;

public class AmmoCard {

    private int blue;
    private int yellow;
    private int red;
    private  boolean powerUpCard;

    public AmmoCard(int blue, int yellow, int red, boolean powerUpCard) {

        this.blue = blue;
        this.yellow = yellow;
        this.red = red;
        this.powerUpCard = powerUpCard;
    }

    public int getBlue() {
        return blue;
    }

    public int getYellow() {
        return yellow;
    }

    public int getRed() {
        return red;
    }

    public boolean isPowerUpCard() {
        return powerUpCard;
    }
}
