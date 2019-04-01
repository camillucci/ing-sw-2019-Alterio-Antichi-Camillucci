package it.polimi.ingsw.model;

public class AmmoCard {

    private int blue;
    private int yellow;
    private int red;
    private PowerupCard puc;


    public void AmmoCard(int blue, int yellow, int red) {

        this.blue = blue;
        this.yellow = yellow;
        this.red = red;
        puc = null;
    }

    public void AmmoCard(int blue, int yellow, int red, PowerupCard puc) {

        this.blue = blue;
        this.yellow = yellow;
        this.red = red;
        this.puc = puc;
    }

}
