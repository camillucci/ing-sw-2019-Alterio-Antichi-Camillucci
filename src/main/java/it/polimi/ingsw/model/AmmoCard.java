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

    public String getName() {
        String temp = "";
        if(blue > 0)
            temp = temp.concat(blue + "B ");
        if(red > 0)
            temp = temp.concat(red + "R ");
        if(yellow > 0)
            temp = temp.concat(yellow + "Y ");
        if(powerUpCard)
            temp = temp.concat("PowerUp");
        return temp;
    }
}
